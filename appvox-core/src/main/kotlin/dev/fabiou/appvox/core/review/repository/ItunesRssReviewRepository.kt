package dev.fabiou.appvox.core.review.repository

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.exception.AppVoxErrorCode
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.review.domain.request.AppStoreReviewRequest
import dev.fabiou.appvox.core.review.domain.request.ReviewRequest
import dev.fabiou.appvox.core.review.domain.result.AppStoreRecentReviewResult
import dev.fabiou.appvox.core.utils.HttpUtils
import dev.fabiou.appvox.core.utils.UrlUtils
import dev.fabiou.appvox.core.utils.impl.HttpUtilsImpl
import java.io.StringReader
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Unmarshaller
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader

internal class ItunesRssReviewRepository(
    private val config: Configuration? = null
) {
    companion object {
        internal const val RSS_REQUEST_URL_DOMAIN = "https://itunes.apple.com"
        internal const val RSS_REQUEST_URL_PATH = "/%s/rss/customerreviews/page=%d/id=%s/sortby=mostrecent/xml"
        internal const val RSS_REQUEST_URL_PARAMS = "?urlDesc=/customerreviews/id=%s/mostrecent/xml"
    }

    private var xif: XMLInputFactory = XMLInputFactory.newFactory()

    private var httpUtils: HttpUtils = HttpUtilsImpl

    init {
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false)
    }

    @Throws(AppVoxException::class)
    fun getReviewsByAppId(request: ReviewRequest, pageNo: Int = 1, nextToken: String? = null): AppStoreRecentReviewResult {
        if (pageNo !in 1..10) {
            throw AppVoxException(AppVoxErrorCode.INVALID_ARGUMENT)
        }

        val requestUrl = nextToken ?: UrlUtils.getUrlDomainByEnv(RSS_REQUEST_URL_DOMAIN) +
        RSS_REQUEST_URL_PATH.format(request.region, pageNo, request.appId) +
        RSS_REQUEST_URL_PARAMS.format(request.appId)
        var responseContent = httpUtils.getRequest(requestUrl = requestUrl, proxyConfig = config?.proxy)

        val result: AppStoreRecentReviewResult
        try {
            val jaxbContext: JAXBContext = JAXBContext.newInstance(AppStoreRecentReviewResult::class.java)
            responseContent = responseContent.replace("&", "&amp;")
            val sr = StringReader(responseContent)
            val xsr: XMLStreamReader = xif.createXMLStreamReader(sr)
            val jaxbUnmarshaller: Unmarshaller = jaxbContext.createUnmarshaller()
            result = jaxbUnmarshaller.unmarshal(xsr) as AppStoreRecentReviewResult
        } catch (e: JAXBException) {
            throw AppVoxException(AppVoxErrorCode.SERIALIZATION)
        }

        return result
    }
}
package dev.fabiou.appvox.core.review.service

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.exception.AppVoxErrorCode
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.review.domain.request.AppStoreReviewRequest
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

internal class AppStoreRecentReviewService(
    private val config: Configuration? = null
) {
    companion object {
        internal const val RSS_REQUEST_URL_DOMAIN  = "https://itunes.apple.com"
        internal const val RSS_REQUEST_URL_PATH  = "/%s/rss/customerreviews/page=%d/id=%s/sortby=mostrecent/xml"
        internal const val RSS_REQUEST_URL_PARAMS  = "?urlDesc=/customerreviews/id=%s/mostrecent/xml"
    }

    private var httpUtils : HttpUtils = HttpUtilsImpl

    @Throws(AppVoxException::class)
    fun getReviewsByAppId(appId: String, request: AppStoreReviewRequest): AppStoreRecentReviewResult {

        if (request.pageNo < 1 || request.pageNo > 10) {
            throw AppVoxException(AppVoxErrorCode.INVALID_ARGUMENT)
        }

        val requestUrl = request.nextToken ?: UrlUtils.getUrlDomainByEnv(RSS_REQUEST_URL_DOMAIN) + RSS_REQUEST_URL_PATH.format(request.region, request.pageNo, appId) + RSS_REQUEST_URL_PARAMS.format(appId)
        var responseContent = httpUtils.getRequest(requestUrl = requestUrl, proxyConfig = config?.proxy)
        val result: AppStoreRecentReviewResult
        try {
            val jaxbContext:  JAXBContext = JAXBContext.newInstance(AppStoreRecentReviewResult::class.java)
            val xif = XMLInputFactory.newFactory()
            xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false)
            responseContent = responseContent.replace("&", "&amp;")
            val sr = StringReader(responseContent)
            val xsr: XMLStreamReader = xif.createXMLStreamReader(sr)
            val jaxbUnmarshaller: Unmarshaller = jaxbContext.createUnmarshaller()
            result = jaxbUnmarshaller.unmarshal(xsr) as AppStoreRecentReviewResult
        } catch (e: JAXBException) {
           throw AppVoxException(AppVoxErrorCode.NETWORK)
        }

        return result
    }
}
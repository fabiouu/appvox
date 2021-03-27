package dev.fabiou.appvox.core.appstore.review

import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.exception.AppVoxErrorCode
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.archive.ReviewRepository
import dev.fabiou.appvox.core.archive.ReviewResult
import dev.fabiou.appvox.core.util.HttpUtil
import dev.fabiou.appvox.core.util.UrlUtil
import java.io.StringReader
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Unmarshaller
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader

internal class ItunesRssReviewRepository(
    private val config: RequestConfiguration? = null
) : ReviewRepository<ItunesRssReviewRequest, ItunesRssReviewResult> {
    companion object {
        internal const val RSS_REQUEST_URL_DOMAIN = "https://itunes.apple.com"
        internal const val RSS_REQUEST_URL_PATH = "/%s/rss/customerreviews/page=%d/id=%s/sortby=mostrecent/xml"
        internal const val RSS_REQUEST_URL_PARAMS = "?urlDesc=/customerreviews/id=%s/mostrecent/xml"
    }

    private var xif = XMLInputFactory.newFactory()

    private val httpUtils = HttpUtil

    init {
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false)
    }

    @Throws(AppVoxException::class)
    override fun getReviewsByAppId(request: ItunesRssReviewRequest): ReviewResult<ItunesRssReviewResult> {
        if (request.pageNo !in 1..10) {
            throw AppVoxException(AppVoxErrorCode.INVALID_ARGUMENT)
        }

        val requestUrl = request.nextToken ?: UrlUtil.getUrlDomainByEnv(RSS_REQUEST_URL_DOMAIN) +
        RSS_REQUEST_URL_PATH.format(request.region, request.pageNo, request.appId) +
        RSS_REQUEST_URL_PARAMS.format(request.appId)
        var responseContent = httpUtils.getRequest(requestUrl = requestUrl, proxyConfig = config?.proxy)

        val result: ItunesRssReviewResult
        try {
            val jaxbContext: JAXBContext = JAXBContext.newInstance(ItunesRssReviewResult::class.java)
            responseContent = responseContent.replace("&", "&amp;")
            val sr = StringReader(responseContent)
            val xsr: XMLStreamReader = xif.createXMLStreamReader(sr)
            val jaxbUnmarshaller: Unmarshaller = jaxbContext.createUnmarshaller()
            result = jaxbUnmarshaller.unmarshal(xsr) as ItunesRssReviewResult
        } catch (e: JAXBException) {
            throw AppVoxException(AppVoxErrorCode.SERIALIZATION)
        }

        return ReviewResult(result = result, nextToken = result.link!!.find { it.rel == "next" }?.href!!)
    }
}
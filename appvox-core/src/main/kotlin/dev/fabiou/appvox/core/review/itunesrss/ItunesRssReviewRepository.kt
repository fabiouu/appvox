package dev.fabiou.appvox.core.review.itunesrss

import dev.fabiou.appvox.core.review.ReviewRepository
import dev.fabiou.appvox.core.review.ReviewResult
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.exception.AppVoxError
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.review.ReviewRequest
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewRequest
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewResult
import dev.fabiou.appvox.core.util.HttpUtil
import dev.fabiou.appvox.core.util.UrlUtil
import java.io.StringReader
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Unmarshaller
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader

internal class ItunesRssReviewRepository(
    private val config: RequestConfiguration
) : ReviewRepository<ItunesRssReviewRequest, ItunesRssReviewResult.Entry> {
    companion object {
        internal const val RSS_REQUEST_URL_DOMAIN = "https://itunes.apple.com"
        internal const val RSS_REQUEST_URL_PATH = "/%s/rss/customerreviews/page=%d/id=%s/sortby=mostrecent/xml"
        internal const val RSS_REQUEST_URL_PARAMS = "?urlDesc=/customerreviews/id=%s/mostrecent/xml"
    }

    private val xif = XMLInputFactory.newFactory()

    private val httpUtils = HttpUtil

    init {
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false)
    }

    @Throws(AppVoxException::class)
    override fun getReviewsByAppId(request: ReviewRequest<ItunesRssReviewRequest>): ReviewResult<ItunesRssReviewResult.Entry> {
        if (request.parameters.pageNo !in 1..10) {
            throw AppVoxException(AppVoxError.INVALID_ARGUMENT)
        }

        val requestUrl = request.nextToken ?: UrlUtil.getUrlDomainByEnv(RSS_REQUEST_URL_DOMAIN) +
        RSS_REQUEST_URL_PATH.format(request.parameters.region.code, request.parameters.pageNo, request.parameters.appId) +
        RSS_REQUEST_URL_PARAMS.format(request.parameters.appId)
        val responseContent = httpUtils.getRequest(requestUrl = requestUrl, proxyConfig = config.proxy)

        val result: ItunesRssReviewResult
        try {
            val jaxbContext: JAXBContext = JAXBContext.newInstance(ItunesRssReviewResult::class.java)
            val cleanResponseContent = responseContent.replace("&", "&amp;")
            val sr = StringReader(cleanResponseContent)
            val xsr: XMLStreamReader = xif.createXMLStreamReader(sr)
            val jaxbUnmarshaller: Unmarshaller = jaxbContext.createUnmarshaller()
            result = jaxbUnmarshaller.unmarshal(xsr) as ItunesRssReviewResult
        } catch (e: JAXBException) {
            throw AppVoxException(AppVoxError.SERIALIZATION)
        }

        return ReviewResult(
            results = result.entry!!,
            nextToken = result.link!!.find { it.rel == "next" }?.href!!
        )
    }
}

package io.appvox.appstore.review

import io.appvox.appstore.review.domain.ItunesRssReviewRequestParameters
import io.appvox.appstore.review.domain.ItunesRssReviewResult
import io.appvox.configuration.RequestConfiguration
import io.appvox.exception.AppVoxError
import io.appvox.exception.AppVoxException
import io.appvox.review.ReviewRequest
import io.appvox.review.ReviewResult
import io.appvox.util.HttpUtil
import java.io.StringReader
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Unmarshaller
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader

internal class ItunesRssReviewRepository(
    private val config: RequestConfiguration
) {
    companion object {
        internal var REQUEST_URL_DOMAIN = "https://itunes.apple.com"
        internal const val REQUEST_URL_PATH = "/%s/rss/customerreviews/page=%d/id=%s/sortby=mostrecent/xml"
        internal const val REQUEST_URL_PARAMS = "?urlDesc=/customerreviews/id=%s/mostrecent/xml"
        internal const val MIN_PAGE_NO = 1
        internal const val MAX_PAGE_NO = 10
    }

    private val xif = XMLInputFactory.newFactory()

    private val httpUtils = HttpUtil

    init {
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false)
    }

    @Throws(AppVoxException::class)
    fun getReviewsByAppId(
        request: ReviewRequest<ItunesRssReviewRequestParameters>
    ): ReviewResult<ItunesRssReviewResult.Entry> {
        if (request.parameters.pageNo !in MIN_PAGE_NO..MAX_PAGE_NO) {
            throw AppVoxException(AppVoxError.INVALID_ARGUMENT)
        }

        val requestUrl = request.nextToken ?: REQUEST_URL_DOMAIN +
        REQUEST_URL_PATH.format(
            request.parameters.region.code,
            request.parameters.pageNo,
            request.parameters.appId) +
        REQUEST_URL_PARAMS.format(
            request.parameters.appId
        )

        val responseContent = httpUtils.getRequest(requestUrl = requestUrl, proxy = config.proxy)
        val result: ItunesRssReviewResult
        try {
            val jaxbContext: JAXBContext = JAXBContext.newInstance(ItunesRssReviewResult::class.java)
            val cleanResponseContent = responseContent.replace("&", "&amp;")
            val sr = StringReader(cleanResponseContent)
            val xsr: XMLStreamReader = xif.createXMLStreamReader(sr)
            val jaxbUnmarshaller: Unmarshaller = jaxbContext.createUnmarshaller()
            result = jaxbUnmarshaller.unmarshal(xsr) as ItunesRssReviewResult
        } catch (e: JAXBException) {
            throw AppVoxException(AppVoxError.DESERIALIZATION, e)
        }

        return ReviewResult(
            results = result.entry!!,
            nextToken = result.link!!.find { it.rel == "next" }?.href!!
        )
    }
}

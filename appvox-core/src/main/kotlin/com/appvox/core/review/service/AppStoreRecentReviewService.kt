package com.appvox.core.review.service

import com.appvox.core.configuration.Configuration
import com.appvox.core.exception.AppVoxErrorCode
import com.appvox.core.exception.AppVoxException
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.result.AppStoreRecentReviewResult
import com.appvox.core.utils.HttpUtils
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
        private const val RSS_REQUEST_URL  = "https://itunes.apple.com/%s/rss/customerreviews" +
            "/page=%d/id=%s/sortby=mostrecent/xml?urlDesc=/customerreviews/id=%s/mostrecent/xml"
    }

    @Throws(AppVoxException::class)
    fun getReviewsByAppId(appId: String, request: AppStoreReviewRequest): AppStoreRecentReviewResult {

        if (request.pageNo < 1 || request.pageNo > 10) {
            throw AppVoxException(AppVoxErrorCode.INVALID_ARGUMENT)
        }

        val requestUrl = request.nextToken ?: RSS_REQUEST_URL.format(request.region, request.pageNo, appId, appId)
        var responseContent = HttpUtils.getRequest(requestUrl, null, config?.proxy)
        var result: AppStoreRecentReviewResult
        try {
            val jaxbContext: JAXBContext = JAXBContext.newInstance(AppStoreRecentReviewResult::class.java)
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
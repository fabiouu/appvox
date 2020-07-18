package com.appvox.appvox.service

import com.appvox.appvox.domain.request.review.AppStoreReviewRequest
import com.appvox.appvox.domain.result.appstore.AppStoreReviewsResult
import com.appvox.appvox.helper.HttpHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service


@Service
class AppStoreReviewService(
        @Autowired
        private val httpHelper : HttpHelper,

        @Value("\${scraper.appStore.request.url:}")
        private val requestUrlPattern : String,

        @Value("\${scraper.appStore.request.urlWithNext:}")
        private val requestUrlWithNext : String,

        @Value("\${scraper.appStore.request.bearerAuth:}")
        private val bearerAuth : String
) {

    fun getReviewsByAppId(appId : String, request : AppStoreReviewRequest) : AppStoreReviewsResult {

        val requestHeaders = HttpHeaders()
        requestHeaders.setBearerAuth(bearerAuth)
        val appStoreRequest: HttpEntity<String> = HttpEntity(requestHeaders)

        var requestUrl : String
        if (request.next != null && request.next.isNotEmpty()) {
            requestUrl = requestUrlWithNext.format(request.next)
        } else {
            requestUrl = requestUrlPattern.format(request.region, appId, request.size)
        }

        val appStoreResponse = httpHelper.getRestTemplate().exchange(
                requestUrl, HttpMethod.GET, appStoreRequest, AppStoreReviewsResult::class.java)

        return appStoreResponse.body!!
    }
}
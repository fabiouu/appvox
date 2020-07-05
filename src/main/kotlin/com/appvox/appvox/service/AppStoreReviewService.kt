package com.appvox.appvox.service

//import com.appvox.appvox.helper.HttpHelper
import com.appvox.appvox.domain.result.appstore.AppStoreReviewResult
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
        private val requestUrl : String,

        @Value("\${scraper.appStore.request.bearerAuth:}")
        private val bearerAuth : String
) {

    fun getReviewsByAppId(
            appId : String, region : String, language : String, sortType : Int,
            reviewCount: Int, token: String? = null) : AppStoreReviewsResult {

        val requestHeaders = HttpHeaders()
        requestHeaders.setBearerAuth(bearerAuth)
        val request: HttpEntity<String> = HttpEntity<String>(requestHeaders)

        val url = requestUrl.format(region, appId, language, reviewCount)
        val appStoreResponse = httpHelper.getRestTemplate().exchange(
                url, HttpMethod.GET, request, AppStoreReviewsResult::class.java)

        return appStoreResponse.body!!
    }
}
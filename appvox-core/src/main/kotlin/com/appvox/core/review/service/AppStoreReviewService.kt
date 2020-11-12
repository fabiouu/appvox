package com.appvox.core.review.service

import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.result.AppStoreReviewsResult
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.getAs

object AppStoreReviewService {

    private const val requestUrlPattern : String = "https://amp-api.apps.apple.com/v1/catalog/%s/apps/%s/reviews?offset=%d&platform=web&additionalPlatforms=appletv,ipad,iphone,mac"
    private const val requestUrlWithNext : String = "https://amp-api.apps.apple.com%s&platform=web&additionalPlatforms=appletv,ipad,iphone,mac"
    private const val bearerToken : String = "eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkNSRjVITkJHUFEifQ.eyJpc3MiOiI4Q1UyNk1LTFM0IiwiaWF0IjoxNjA0NDIzNjk4LCJleHAiOjE2MDc0NDc2OTh9.lNJUIJvv99NIs1ea_qiv5lJyEsBNrppZMJiex4drI_dGWzkT5HB-DBYyw8aaYwlea8CkQVAqNLe3790UiD6XoQ";

    fun getReviewsByAppId(appId : String, request : AppStoreReviewRequest) : AppStoreReviewsResult? {

        var requestUrl : String
        if (request.next != null && request.next.isNotEmpty()) {
            requestUrl = requestUrlWithNext.format(request.next)
        } else {
            requestUrl = requestUrlPattern.format(request.region, appId, request.size)
        }

        val (request, response, result) = requestUrl
                .httpGet()
                .authentication()
                .bearer(bearerToken)
                .responseObject<AppStoreReviewsResult>()

        val appStoreResult = result.getAs<AppStoreReviewsResult>()
        return appStoreResult
    }
}
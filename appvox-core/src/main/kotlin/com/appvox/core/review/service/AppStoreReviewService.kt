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
    private const val bearerToken : String = "eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IldlYlBsYXlLaWQifQ.eyJpc3MiOiJBTVBXZWJQbGF5IiwiaWF0IjoxNTgyMTQzMTIxLCJleHAiOjE1OTc2OTUxMjF9.j3MAuNa0ZfVVOsBtsGFBmNwT4jPrKu2Alp5PzdhQC3Id--pboI9GqrysOSj2bfg0P-iJboXsg3R_dWr1TQ3pwg"

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
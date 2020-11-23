package com.appvox.core.review.service

import com.appvox.core.configuration.Configuration
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.result.AppStoreReviewResult
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.getAs
import java.net.InetSocketAddress
import java.net.Proxy

internal class AppStoreReviewService(
        val configuration: Configuration? = null
) {

    private val requestUrlPattern : String = "https://amp-api.apps.apple.com/v1/catalog/%s/apps/%s/reviews?offset=%d&platform=web&additionalPlatforms=appletv,ipad,iphone,mac"
    private val requestUrlWithNext : String = "https://amp-api.apps.apple.com%s&platform=web&additionalPlatforms=appletv,ipad,iphone,mac"

    fun getReviewsByAppId(appId : String, request : AppStoreReviewRequest) : AppStoreReviewResult? {
        var requestUrl : String
        if (request.next != null && request.next.isNotEmpty()) {
            requestUrl = requestUrlWithNext.format(request.next)
        } else {
            requestUrl = requestUrlPattern.format(request.region, appId, request.size)
        }

        if (null != configuration) {
            val addr = InetSocketAddress(configuration.proxy?.host!!, configuration.proxy?.port!!)
            FuelManager.instance.proxy = Proxy(Proxy.Type.HTTP, addr)
        }

        val (request, response, result) = requestUrl
                .httpGet()
                .authentication()
                .bearer(request.bearerToken)
                .responseObject<AppStoreReviewResult>()

        val appStoreResult = result.getAs<AppStoreReviewResult>()
        return appStoreResult
    }

    fun getBearerToken(appId: String, region: String): String {

        if (null != configuration) {
            val addr = InetSocketAddress(configuration.proxy?.host!!, configuration.proxy?.port!!)
            FuelManager.instance.proxy = Proxy(Proxy.Type.HTTP, addr)
        }

        val url = "https://apps.apple.com/us/app/twitter/id333903271"
        val (request, response, result) = url.httpGet().responseString()
        val body = result.get()
        val regex = "token%22%3A%22(.+?)%22".toRegex()
        val tokenMatches = regex.find(body)
        val tokenMatch = tokenMatches?.groupValues?.get(1)
        return tokenMatch.orEmpty()
    }
}
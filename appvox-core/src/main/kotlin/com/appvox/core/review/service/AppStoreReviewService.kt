package com.appvox.core.review.service

import com.appvox.core.configuration.ProxyConfiguration
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
        val configuration: ProxyConfiguration? = null
) {

    private val requestReviewSize = 10
    private val appHomepageUrlPattern = "https://apps.apple.com/%s/app/id%s"
    private val requestUrlWithParams = "https://amp-api.apps.apple.com/v1/catalog/%s/apps/%s/reviews?offset=%d&platform=web&additionalPlatforms=appletv,ipad,iphone,mac"
    private val requestUrlWithNext = "https://amp-api.apps.apple.com%s&platform=web&additionalPlatforms=appletv,ipad,iphone,mac"
    private val bearerTokenRegexPattern = "token%22%3A%22(.+?)%22"

    fun getReviewsByAppId(appId : String, request : AppStoreReviewRequest) : AppStoreReviewResult? {

        val requestUrl = if (request.next.isNullOrEmpty()) {
            requestUrlWithParams.format(request.region, appId, requestReviewSize)
        } else {
            requestUrlWithNext.format(request.next)
        }

        if (null != configuration) {
            val addr = InetSocketAddress(configuration.host!!, configuration.port!!)
            FuelManager.instance.proxy = Proxy(Proxy.Type.HTTP, addr)
        }

        val (request, response, result) = requestUrl
                .httpGet()
                .authentication()
                .bearer(request.bearerToken)
                .responseObject<AppStoreReviewResult>()

        return result.getAs<AppStoreReviewResult>()
    }

    fun getBearerToken(appId: String, region: String): String {
        if (null != configuration) {
            val addr = InetSocketAddress(configuration.host!!, configuration.port!!)
            FuelManager.instance.proxy = Proxy(Proxy.Type.HTTP, addr)
        }

        val url = appHomepageUrlPattern.format(region, appId)
        val (request, response, result) = url.httpGet().responseString()
        val body = result.get()
        val regex = bearerTokenRegexPattern.toRegex()
        val tokenMatches = regex.find(body)
        val tokenMatch = tokenMatches?.groupValues?.get(1)

        return tokenMatch.orEmpty()
    }
}
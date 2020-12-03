package com.appvox.core.review.service

import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.result.AppStoreReviewResult
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.*


internal class AppStoreReviewService(
    val configuration: ProxyConfiguration? = null
) {

    private val requestReviewSize = 10
    private val appHomepageUrlPattern = "https://apps.apple.com/%s/app/id%s"
    private val requestUrlWithParams = "https://amp-api.apps.apple.com/v1/catalog/%s/apps/%s/reviews?offset=%d&platform=web&additionalPlatforms=appletv,ipad,iphone,mac"
    private val requestUrlWithNext = "https://amp-api.apps.apple.com%s&platform=web&additionalPlatforms=appletv,ipad,iphone,mac"
    private val bearerTokenRegexPattern = "token%22%3A%22(.+?)%22"

    fun getReviewsByAppId(appId: String, request: AppStoreReviewRequest) : AppStoreReviewResult? {

        val requestUrl = if (request.nextToken.isNullOrEmpty()) {
            requestUrlWithParams.format(request.region, appId, requestReviewSize)
        } else {
            requestUrlWithNext.format(request.nextToken)
        }

        var conn : URLConnection
        if (null != configuration) {
            val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(configuration.host!!, configuration.port!!.toInt()))
            conn = URL(requestUrl).openConnection(proxy)
            if (null != configuration.user && null != configuration.password) {
                val authenticator: Authenticator = object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication? {
                        return PasswordAuthentication(configuration.user, configuration.password.toCharArray())
                    }
                }
                Authenticator.setDefault(authenticator)
            }
        } else {
            conn = URL(requestUrl).openConnection()
        }

        conn.setRequestProperty("Authorization", "Bearer " + request.bearerToken)
        conn.setRequestProperty("Content-Type", "application/json")

        var response = StringBuffer()
        val reader = BufferedReader(InputStreamReader(conn.getInputStream()));
        while (true) {
            val line = reader.readLine() ?: break
            response.append(line);
        }
        reader.close()

        val result = ObjectMapper().readValue(response.toString(), AppStoreReviewResult::class.java)
        return result
    }

    fun getBearerToken(appId: String, region: String): String {
        val requestUrl = appHomepageUrlPattern.format(region, appId)

        var conn : URLConnection
        if (null != configuration) {
            val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(configuration.host!!, configuration.port!!.toInt()))
            conn = URL(requestUrl).openConnection(proxy)
            if (null != configuration.user && null != configuration.password) {
                val authenticator: Authenticator = object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication? {
                        return PasswordAuthentication(configuration.user, configuration.password.toCharArray())
                    }
                }
                Authenticator.setDefault(authenticator)
            }
        } else {
            conn = URL(requestUrl).openConnection()
        }

        var response = StringBuffer()
        val reader = BufferedReader(InputStreamReader(conn.getInputStream()));
        while (true) {
            val line = reader.readLine() ?: break
            response.append(line);
        }
        reader.close()

        val body = response.toString()
        val regex = bearerTokenRegexPattern.toRegex()
        val tokenMatches = regex.find(body)
        val tokenMatch = tokenMatches?.groupValues?.get(1)

        return tokenMatch.orEmpty()
    }
}
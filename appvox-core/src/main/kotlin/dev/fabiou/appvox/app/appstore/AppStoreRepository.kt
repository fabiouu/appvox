package dev.fabiou.appvox.app.appstore

import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.exception.AppVoxException
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion
import dev.fabiou.appvox.util.HttpUtil

internal class AppStoreRepository(
    private val config: RequestConfiguration
) {
    companion object {
        internal var APP_HP_URL_DOMAIN = "https://apps.apple.com"
        internal const val APP_HP_URL_PATH = "/%s/app/id%s"
        private const val BEARER_TOKEN_REGEX_PATTERN = "token%22%3A%22(.+?)%22"
    }

    private val httpUtils = HttpUtil

    @Throws(AppVoxException::class)
    fun getBearerToken(appId: String, region: AppStoreRegion): String {
        val requestUrl = APP_HP_URL_DOMAIN + APP_HP_URL_PATH.format(region.code, appId)
        val responseContent = httpUtils.getRequest(requestUrl = requestUrl, proxyConfig = config.proxy)
        val regex = BEARER_TOKEN_REGEX_PATTERN.toRegex()
        val tokenMatches = regex.find(responseContent)
        val tokenMatch = tokenMatches?.groupValues?.get(1)
        return tokenMatch.orEmpty()
    }
}

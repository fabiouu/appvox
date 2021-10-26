package io.appvox.appstore.app

import io.appvox.appstore.review.constant.AppStoreRegion
import io.appvox.core.configuration.RequestConfiguration
import io.appvox.core.util.HttpUtil
import io.appvox.core.util.memoize

internal class AppStoreRepository(
    private val config: RequestConfiguration
) {
    companion object {
        internal var APP_HP_URL_DOMAIN = "https://apps.apple.com"
        internal const val APP_HP_URL_PATH = "/%s/app/id%s"
        private const val BEARER_TOKEN_REGEX_PATTERN = "token%22%3A%22(.+?)%22"
    }

    private val httpUtils = HttpUtil

    fun getBearerToken(appId: String, region: AppStoreRegion): String {
        val requestUrl = APP_HP_URL_DOMAIN + APP_HP_URL_PATH.format(region.code, appId)
        val responseContent = httpUtils.getRequest(requestUrl = requestUrl, proxy = config.proxy)
        val regex = BEARER_TOKEN_REGEX_PATTERN.toRegex()
        val tokenMatches = regex.find(responseContent)
        val tokenMatch = tokenMatches?.groupValues?.get(1)
        return tokenMatch.orEmpty()
    }

    val memoizedBearerToken = { x: String, y: AppStoreRegion -> this.getBearerToken(x, y) }.memoize()
}

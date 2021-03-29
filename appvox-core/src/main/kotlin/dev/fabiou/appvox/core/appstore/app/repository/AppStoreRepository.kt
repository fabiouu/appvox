package dev.fabiou.appvox.core.appstore.app.repository

import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.util.HttpUtil
import dev.fabiou.appvox.core.util.UrlUtil


internal class AppStoreRepository(
        private val config: RequestConfiguration? = null
) {
    companion object {
        internal const val APP_HP_URL_DOMAIN = "https://apps.apple.com"
        internal const val APP_HP_URL_PATH = "/%s/app/id%s"

        internal const val REQUEST_URL_DOMAIN = "https://amp-api.apps.apple.com"

        private const val BEARER_TOKEN_REGEX_PATTERN = "token%22%3A%22(.+?)%22"
    }

    private val httpUtils = HttpUtil

    @Throws(AppVoxException::class)
    fun getBearerToken(appId: String, region: String): String {
        val requestUrl = UrlUtil.getUrlDomainByEnv(APP_HP_URL_DOMAIN) + APP_HP_URL_PATH.format(region, appId)
        val responseContent = httpUtils.getRequest(requestUrl = requestUrl, proxyConfig = config?.proxy)
        val regex = BEARER_TOKEN_REGEX_PATTERN.toRegex()
        val tokenMatches = regex.find(responseContent)
        val tokenMatch = tokenMatches?.groupValues?.get(1)
        return tokenMatch.orEmpty()
    }
}
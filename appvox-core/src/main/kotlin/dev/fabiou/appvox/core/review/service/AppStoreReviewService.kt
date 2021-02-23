package dev.fabiou.appvox.core.review.service

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.review.domain.request.AppStoreReviewRequest
import dev.fabiou.appvox.core.review.domain.result.AppStoreReviewResult
import dev.fabiou.appvox.core.utils.HttpUtils
import dev.fabiou.appvox.core.utils.impl.HttpUtilsImpl
import com.fasterxml.jackson.databind.ObjectMapper
import dev.fabiou.appvox.core.utils.UrlUtils


open class AppStoreReviewService(
    private val config: Configuration? = null
) {
    companion object {
        internal const val REQUEST_REVIEW_SIZE = 10

        internal const val APP_HP_URL_DOMAIN = "https://apps.apple.com"
        internal const val APP_HP_URL_PATH = "/%s/app/id%s"

        internal const val REQUEST_URL_DOMAIN = "https://amp-api.apps.apple.com"
        internal const val REQUEST_URL_PATH = "/v1/catalog/%s/apps/%s/reviews"
        internal const val REQUEST_URL_PARAMS_PREFIX = "?offset=%d"
        internal const val REQUEST_URL_PARAMS = "&platform=web&additionalPlatforms=appletv,ipad,iphone,mac"

        private const val BEARER_TOKEN_REGEX_PATTERN = "token%22%3A%22(.+?)%22"
    }

    private var httpUtils : HttpUtils = HttpUtilsImpl

    @Throws(AppVoxException::class)
    fun getReviewsByAppId(appId: String, request: AppStoreReviewRequest): AppStoreReviewResult {
        val requestUrl = if (request.nextToken.isNullOrEmpty()) {
            UrlUtils.getUrlDomainByEnv(REQUEST_URL_DOMAIN) + REQUEST_URL_PATH.format(request.region, appId) + REQUEST_URL_PARAMS_PREFIX.format(REQUEST_REVIEW_SIZE) + REQUEST_URL_PARAMS
        } else {
            UrlUtils.getUrlDomainByEnv(REQUEST_URL_DOMAIN) + request.nextToken + REQUEST_URL_PARAMS
        }
        val responseContent = httpUtils.getRequest(
                requestUrl = requestUrl, bearerToken = request.bearerToken, proxyConfig = config?.proxy)
        val result = ObjectMapper().readValue(responseContent, AppStoreReviewResult::class.java)
        return result
    }

    @Throws(AppVoxException::class)
    fun getBearerToken(appId: String, region: String): String {
        val requestUrl = UrlUtils.getUrlDomainByEnv(APP_HP_URL_DOMAIN) + APP_HP_URL_PATH.format(region, appId)
        val responseContent = httpUtils.getRequest(requestUrl = requestUrl, proxyConfig = config?.proxy)
        val regex = BEARER_TOKEN_REGEX_PATTERN.toRegex()
        val tokenMatches = regex.find(responseContent)
        val tokenMatch = tokenMatches?.groupValues?.get(1)
        return tokenMatch.orEmpty()
    }
}
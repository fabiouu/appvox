package dev.fabiou.appvox.core.appstore.review.repository

import com.fasterxml.jackson.databind.ObjectMapper
import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewRequest
import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewResult
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.util.HttpUtil
import dev.fabiou.appvox.core.util.UrlUtil


internal class AppStoreReviewRepository(
        private val config: RequestConfiguration? = null
) {
    companion object {
        internal const val REQUEST_REVIEW_SIZE = 10

        internal const val APP_HP_URL_PATH = "/%s/app/id%s"

        internal const val REQUEST_URL_DOMAIN = "https://amp-api.apps.apple.com"
        internal const val REQUEST_URL_PATH = "/v1/catalog/%s/apps/%s/reviews"
        internal const val REQUEST_URL_PARAMS_PREFIX = "?offset=%d"
        internal const val REQUEST_URL_PARAMS = "&platform=web&additionalPlatforms=appletv,ipad,iphone,mac"
    }

    private val httpUtils = HttpUtil

    @Throws(AppVoxException::class)
    fun getReviewsByAppId(request: AppStoreReviewRequest): AppStoreReviewResult {
        val requestUrl = UrlUtil.getUrlDomainByEnv(REQUEST_URL_DOMAIN) + if (request.nexitToken.isNullOrEmpty()) {
            REQUEST_URL_PATH.format(request.region, request.appId) +
                    REQUEST_URL_PARAMS_PREFIX.format(REQUEST_REVIEW_SIZE) + REQUEST_URL_PARAMS
        } else {
            request.nextToken + REQUEST_URL_PARAMS
        }

        val responseContent = httpUtils.getRequest(
                requestUrl = requestUrl,
                bearerToken = request.bearerToken,
                proxyConfig = config?.proxy)

        return ObjectMapper().readValue(responseContent, AppStoreReviewResult::class.java)
    }
}
package dev.fabiou.appvox.core.review.appstore

import com.fasterxml.jackson.databind.ObjectMapper
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.review.ReviewRepository
import dev.fabiou.appvox.core.review.ReviewRequest
import dev.fabiou.appvox.core.review.ReviewResult
import dev.fabiou.appvox.core.review.appstore.domain.AppStoreReviewRequest
import dev.fabiou.appvox.core.review.appstore.domain.AppStoreReviewResult
import dev.fabiou.appvox.core.util.HttpUtil
import dev.fabiou.appvox.core.util.UrlUtil


internal class AppStoreReviewRepository(
        private val config: RequestConfiguration
) : ReviewRepository<AppStoreReviewRequest, AppStoreReviewResult.AppStoreReview> {
    companion object {
        internal const val REQUEST_REVIEW_SIZE = 10
        internal const val REQUEST_URL_DOMAIN = "https://amp-api.apps.apple.com"
        internal const val REQUEST_URL_PATH = "/v1/catalog/%s/apps/%s/reviews"
        internal const val REQUEST_URL_PARAMS_PREFIX = "?offset=%d"
        internal const val REQUEST_URL_PARAMS = "&platform=web&additionalPlatforms=appletv,ipad,iphone,mac"
    }

    private val httpUtils = HttpUtil

    @Throws(AppVoxException::class)
    override fun getReviewsByAppId(request: ReviewRequest<AppStoreReviewRequest>): ReviewResult<AppStoreReviewResult.AppStoreReview> {
        val requestUrl = UrlUtil.getUrlDomainByEnv(REQUEST_URL_DOMAIN) + if (request.nextToken.isNullOrEmpty()) {
            REQUEST_URL_PATH.format(request.parameters.region.code, request.parameters.appId) +
                    REQUEST_URL_PARAMS_PREFIX.format(REQUEST_REVIEW_SIZE) + REQUEST_URL_PARAMS
        } else {
            request.nextToken + REQUEST_URL_PARAMS
        }

        val responseContent = httpUtils.getRequest(
                requestUrl = requestUrl,
                bearerToken = request.parameters.bearerToken,
                proxyConfig = config.proxy)

        val result = ObjectMapper().readValue(responseContent, AppStoreReviewResult::class.java)
        return ReviewResult(
            results = result.data,
            nextToken = result.next
        )
    }
}
package io.appvox.appstore.review

import io.appvox.configuration.RequestConfiguration
import io.appvox.exception.AppVoxException
import io.appvox.review.ReviewRequest
import io.appvox.review.ReviewResult
import io.appvox.appstore.review.domain.AppStoreReviewRequestParameters
import io.appvox.appstore.review.domain.AppStoreReviewResult
import io.appvox.util.HttpUtil
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

val lenientJson = Json { isLenient = true }

internal class AppStoreReviewRepository(
    private val config: RequestConfiguration
) {
    companion object {
        internal const val REQUEST_REVIEW_SIZE = 10
        internal var REQUEST_URL_DOMAIN = "https://amp-api.apps.apple.com"
        internal const val REQUEST_URL_PATH = "/v1/catalog/%s/apps/%s/reviews"
        internal const val REQUEST_URL_PARAMS_PREFIX = "?offset=%d"
        internal const val REQUEST_URL_PARAMS = "&platform=web&additionalPlatforms=appletv,ipad,iphone,mac"
    }

    private val httpUtils = HttpUtil

    @Throws(AppVoxException::class)
    fun getReviewsByAppId(
        request: ReviewRequest<AppStoreReviewRequestParameters>
    ): ReviewResult<AppStoreReviewResult.AppStoreReview> {
        val requestUrl = REQUEST_URL_DOMAIN + if (request.nextToken.isNullOrEmpty()) {
            REQUEST_URL_PATH.format(request.parameters.region.code, request.parameters.appId) +
                REQUEST_URL_PARAMS_PREFIX.format(REQUEST_REVIEW_SIZE) + REQUEST_URL_PARAMS
        } else {
            request.nextToken + REQUEST_URL_PARAMS
        }

        val responseContent = httpUtils.getRequest(
            requestUrl = requestUrl,
            bearerToken = request.parameters.bearerToken,
            proxy = config.proxy
        )

        val result = lenientJson.decodeFromString<AppStoreReviewResult>(responseContent)
        return ReviewResult(
            results = result.data,
            nextToken = result.next
        )
    }
}

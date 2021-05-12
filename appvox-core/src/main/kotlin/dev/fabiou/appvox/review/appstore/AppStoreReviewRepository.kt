package dev.fabiou.appvox.review.appstore

import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.exception.AppVoxException
import dev.fabiou.appvox.review.ReviewRepository
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.ReviewResult
import dev.fabiou.appvox.review.appstore.domain.AppStoreReviewRequestParameters
import dev.fabiou.appvox.review.appstore.domain.AppStoreReviewResult
import dev.fabiou.appvox.util.HttpUtil
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

val lenientJson = Json { isLenient = true }

internal class AppStoreReviewRepository(
    private val config: RequestConfiguration
) : ReviewRepository<AppStoreReviewRequestParameters, AppStoreReviewResult.AppStoreReview> {
    companion object {
        internal const val REQUEST_REVIEW_SIZE = 10
        internal var REQUEST_URL_DOMAIN = "https://amp-api.apps.apple.com"
        internal const val REQUEST_URL_PATH = "/v1/catalog/%s/apps/%s/reviews"
        internal const val REQUEST_URL_PARAMS_PREFIX = "?offset=%d"
        internal const val REQUEST_URL_PARAMS = "&platform=web&additionalPlatforms=appletv,ipad,iphone,mac"
    }

    private val httpUtils = HttpUtil

    @Throws(AppVoxException::class)
    override fun getReviewsByAppId(
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

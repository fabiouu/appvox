package dev.fabiou.appvox

import dev.fabiou.appvox.appstore.review.service.AppStoreReviewService
import dev.fabiou.appvox.configuration.Constant.MAX_RETRY_ATTEMPTS
import dev.fabiou.appvox.configuration.Constant.MIN_REQUEST_DELAY
import dev.fabiou.appvox.configuration.Constant.MIN_RETRY_DELAY
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.exception.AppVoxError
import dev.fabiou.appvox.exception.AppVoxException
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.appstore.AppStoreReviewConverter
import dev.fabiou.appvox.review.appstore.domain.AppStoreReview
import dev.fabiou.appvox.review.appstore.domain.AppStoreReviewRequestParameters
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion.UNITED_STATES
import dev.fabiou.appvox.util.HttpUtil
import dev.fabiou.appvox.util.retryRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * This class consists of the main methods for interacting with Apple App Store
 */
class AppStore(
    val config: RequestConfiguration = RequestConfiguration(delay = MIN_REQUEST_DELAY)
) {
    private val appStoreReviewService = AppStoreReviewService(config)

    private val appStoreReviewConverter = AppStoreReviewConverter()

    init {
        config.proxyAuthentication?.let {
            HttpUtil.setAuthenticator(it.userName, it.password)
        }
    }

    /**
     * Returns a flow of Reviews from iTunes RSS Feed
     */
    fun reviews(
        appId: String,
        region: AppStoreRegion = UNITED_STATES
    ): Flow<AppStoreReview> = flow {

        if (config.delay < MIN_REQUEST_DELAY) {
            throw AppVoxException(AppVoxError.REQ_DELAY_TOO_SHORT)
        }

        val initialRequest = ReviewRequest(AppStoreReviewRequestParameters(appId, region))
        var request = initialRequest
        do {
            val response = retryRequest(MAX_RETRY_ATTEMPTS, MIN_RETRY_DELAY) {
                appStoreReviewService.getReviewsByAppId(request)
            }
            request = request.copy(request.parameters, response.nextToken)
            val reviews = appStoreReviewConverter.toResponse(response.results)
            reviews.forEach { review ->
                emit(review)
            }
            delay(timeMillis = config.delay.toLong())
        } while (request.nextToken != null)
    }
}

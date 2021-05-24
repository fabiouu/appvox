package dev.fabiou.appvox

import dev.fabiou.appvox.configuration.Constant.MIN_REQUEST_DELAY
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.exception.AppVoxError
import dev.fabiou.appvox.exception.AppVoxException
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.appstore.AppStoreReviewService
import dev.fabiou.appvox.review.appstore.domain.AppStoreReview
import dev.fabiou.appvox.review.appstore.domain.AppStoreReviewRequestParameters
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion.UNITED_STATES
import dev.fabiou.appvox.util.HttpUtil
import kotlinx.coroutines.flow.Flow

/**
 * This class consists of the main methods for interacting with Apple App Store
 */
class AppStore(
    val config: RequestConfiguration = RequestConfiguration(delay = MIN_REQUEST_DELAY)
) {
    private val appStoreReviewService = AppStoreReviewService(config)

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
    ): Flow<AppStoreReview> {
        if (config.delay < MIN_REQUEST_DELAY) {
            throw AppVoxException(AppVoxError.REQ_DELAY_TOO_SHORT)
        }
        val initialRequest = ReviewRequest(AppStoreReviewRequestParameters(appId, region))
        return appStoreReviewService.getReviewsByAppId(initialRequest)
    }
}

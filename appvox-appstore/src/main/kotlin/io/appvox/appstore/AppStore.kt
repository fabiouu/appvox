package io.appvox.appstore

import io.appvox.appstore.review.AppStoreReviewService
import io.appvox.appstore.review.domain.AppStoreReview
import io.appvox.appstore.review.domain.AppStoreReviewRequestParameters
import io.appvox.core.configuration.Constant.MIN_REQUEST_DELAY
import io.appvox.core.configuration.RequestConfiguration
import io.appvox.core.exception.AppVoxError
import io.appvox.core.exception.AppVoxException
import io.appvox.core.review.ReviewRequest
import io.appvox.core.util.HttpUtil
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
    fun reviews(parameters: AppStoreReviewRequestParameters.Builder.() -> Unit): Flow<AppStoreReview> {
        if (config.delay < MIN_REQUEST_DELAY) {
            throw AppVoxException(AppVoxError.REQ_DELAY_TOO_SHORT)
        }
        val appStoreRequest = AppStoreReviewRequestParameters.Builder().apply(parameters).build()
        val initialRequest = ReviewRequest(appStoreRequest)
        return appStoreReviewService.getReviewsByAppId(initialRequest)
    }
}

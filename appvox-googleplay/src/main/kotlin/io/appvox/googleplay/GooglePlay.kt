package io.appvox.googleplay

import io.appvox.core.configuration.Constant.MIN_REQUEST_DELAY
import io.appvox.core.configuration.RequestConfiguration
import io.appvox.core.exception.AppVoxError
import io.appvox.core.exception.AppVoxException
import io.appvox.core.review.ReviewRequest
import io.appvox.core.util.HttpUtil
import io.appvox.googleplay.review.GooglePlayReviewService
import io.appvox.googleplay.review.domain.GooglePlayReview
import io.appvox.googleplay.review.domain.GooglePlayReviewRequestParameters
import kotlinx.coroutines.flow.Flow

/**
 * This class consists of the main methods for interacting with Google Play
 */
class GooglePlay(
    val config: RequestConfiguration = RequestConfiguration(delay = MIN_REQUEST_DELAY)
) {
    @PublishedApi internal val googlePlayReviewService = GooglePlayReviewService(config)

    init {
        config.proxyAuthentication?.let {
            HttpUtil.setAuthenticator(it.userName, it.password)
        }
    }

    /**
     * Returns a Kotlin Flow of reviews
     * @param deviceName The list of devices supported by Google Play can be found here:
     * https://storage.googleapis.com/play_public/supported_devices.html
     */
    fun reviews(parameters: GooglePlayReviewRequestParameters.Builder.() -> Unit): Flow<GooglePlayReview> {
        if (config.delay < MIN_REQUEST_DELAY) {
            throw AppVoxException(AppVoxError.REQ_DELAY_TOO_SHORT)
        }
        val googlePlayRequest = GooglePlayReviewRequestParameters.Builder().apply(parameters).build()
        val initialRequest = ReviewRequest(googlePlayRequest)
        return googlePlayReviewService.getReviewsByAppId(initialRequest)
    }
}

package dev.fabiou.appvox

import dev.fabiou.appvox.configuration.Constant.DEFAULT_BATCH_SIZE
import dev.fabiou.appvox.configuration.Constant.MIN_REQUEST_DELAY
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.exception.AppVoxError
import dev.fabiou.appvox.exception.AppVoxException
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.googleplay.GooglePlayReviewService
import dev.fabiou.appvox.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.review.googleplay.constant.GooglePlaySortType
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewRequestParameters
import dev.fabiou.appvox.util.HttpUtil
import kotlinx.coroutines.flow.Flow

/**
 * This class consists of the main methods for interacting with Google Play
 */
class GooglePlay(
    val config: RequestConfiguration = RequestConfiguration(delay = MIN_REQUEST_DELAY)
) {
    private val googlePlayReviewService = GooglePlayReviewService(config)

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
    fun reviews(
        appId: String,
        language: GooglePlayLanguage = GooglePlayLanguage.ENGLISH_US,
        sortType: GooglePlaySortType = GooglePlaySortType.RECENT,
        rating: Int? = null,
        fetchHistory: Boolean = false,
        deviceName: String? = null,
        batchSize: Int = DEFAULT_BATCH_SIZE
    ): Flow<GooglePlayReview> {
        if (config.delay < MIN_REQUEST_DELAY) {
            throw AppVoxException(AppVoxError.REQ_DELAY_TOO_SHORT)
        }
        val initialRequest = ReviewRequest(
            GooglePlayReviewRequestParameters(appId, language, sortType, rating, fetchHistory, batchSize, deviceName))
        return googlePlayReviewService.getReviewsByAppId(initialRequest)
    }
}

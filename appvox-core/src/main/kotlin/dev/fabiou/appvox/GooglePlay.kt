package dev.fabiou.appvox

import dev.fabiou.appvox.configuration.Constant.DEFAULT_BATCH_SIZE
import dev.fabiou.appvox.configuration.Constant.MAX_RETRY_ATTEMPTS
import dev.fabiou.appvox.configuration.Constant.MIN_REQUEST_DELAY
import dev.fabiou.appvox.configuration.Constant.MIN_RETRY_DELAY
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.exception.AppVoxError
import dev.fabiou.appvox.exception.AppVoxException
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.googleplay.GooglePlayReviewConverter
import dev.fabiou.appvox.review.googleplay.GooglePlayReviewService
import dev.fabiou.appvox.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.review.googleplay.constant.GooglePlaySortType
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewRequestParameters
import dev.fabiou.appvox.util.HttpUtil
import dev.fabiou.appvox.util.retryRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * This class consists of the main methods for interacting with Google Play
 */
class GooglePlay(
    val config: RequestConfiguration = RequestConfiguration(delay = MIN_REQUEST_DELAY)
) {

    private val googlePlayReviewService = GooglePlayReviewService(config)

    private val googlePlayReviewConverter = GooglePlayReviewConverter()

    init {
        config.proxyAuthentication?.let {
            HttpUtil.setAuthenticator(it.userName, it.password)
        }
    }

    /**
     * Returns a Kotlin Flow of reviews
     */
    fun reviews(
        appId: String,
        language: GooglePlayLanguage = GooglePlayLanguage.ENGLISH_US,
        sortType: GooglePlaySortType = GooglePlaySortType.RECENT,
        batchSize: Int = DEFAULT_BATCH_SIZE
    ): Flow<GooglePlayReview> = flow {
        if (config.delay < MIN_REQUEST_DELAY) {
            throw AppVoxException(AppVoxError.REQ_DELAY_TOO_SHORT)
        }

        val initialRequest = ReviewRequest(GooglePlayReviewRequestParameters(appId, language, sortType, batchSize))
        var request = initialRequest
        do {
            val response = retryRequest(MAX_RETRY_ATTEMPTS, MIN_RETRY_DELAY) {
                googlePlayReviewService.getReviewsByAppId(request)
            }
            request = request.copy(request.parameters, response.nextToken)
            val reviews = googlePlayReviewConverter.toResponse(response.results)
            reviews.forEach { review ->
                emit(review)
            }
            delay(timeMillis = config.delay.toLong())
        } while (request.nextToken != null)
    }
}

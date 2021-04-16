package dev.fabiou.appvox.core

import dev.fabiou.appvox.core.configuration.Constant.MIN_REQUEST_DELAY
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.exception.AppVoxError
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.review.ReviewIterator
import dev.fabiou.appvox.core.review.ReviewRequest
import dev.fabiou.appvox.core.review.googleplay.GooglePlayReviewConverter
import dev.fabiou.appvox.core.review.googleplay.GooglePlayReviewService
import dev.fabiou.appvox.core.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.review.googleplay.constant.GooglePlaySortType
import dev.fabiou.appvox.core.review.googleplay.domain.GooglePlayReview
import dev.fabiou.appvox.core.review.googleplay.domain.GooglePlayReviewRequest
import dev.fabiou.appvox.core.util.HttpUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GooglePlay(
    val config: RequestConfiguration = RequestConfiguration(delay = MIN_REQUEST_DELAY)
) {

    companion object {
        private const val DEFAULT_BATCH_SIZE = 44
    }

    private val googlePlayReviewService = GooglePlayReviewService(config)

    private val googlePlayReviewConverter = GooglePlayReviewConverter()

    init {
        if (config.proxy?.user != null && config.proxy.password != null) {
            HttpUtil.setAuthenticator(config.proxy.user, config.proxy.password)
        }
    }

    fun reviews(
        appId: String,
        language: GooglePlayLanguage = GooglePlayLanguage.ENGLISH_US,
        sortType: GooglePlaySortType = GooglePlaySortType.RECENT,
        batchSize: Int = DEFAULT_BATCH_SIZE
    ): Flow<GooglePlayReview.GooglePlayReview> = flow {

        if (config.delay < MIN_REQUEST_DELAY) {
            throw AppVoxException(AppVoxError.REQ_DELAY_TOO_SHORT)
        }

        val iterator = ReviewIterator(
            converter = googlePlayReviewConverter,
            service = googlePlayReviewService,
            request = ReviewRequest(
                GooglePlayReviewRequest(
                    appId = appId,
                    language = language,
                    sortType = sortType,
                    batchSize = batchSize
                )
            )
        )

        iterator.forEach { reviews ->
            delay(config.delay.toLong())
            reviews.forEach { review ->
                emit(review)
            }
        }
    }
}

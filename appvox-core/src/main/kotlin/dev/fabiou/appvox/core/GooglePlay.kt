package dev.fabiou.appvox.core

import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.review.ReviewIterator
import dev.fabiou.appvox.core.review.ReviewRequest
import dev.fabiou.appvox.core.review.googleplay.GooglePlayReviewConverter
import dev.fabiou.appvox.core.review.googleplay.GooglePlayReviewService
import dev.fabiou.appvox.core.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.review.googleplay.constant.GooglePlaySortType
import dev.fabiou.appvox.core.review.googleplay.domain.GooglePlayReview
import dev.fabiou.appvox.core.review.googleplay.domain.GooglePlayReviewRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GooglePlay(
    val config: RequestConfiguration = RequestConfiguration()
) {
    private var googlePlayReviewService = GooglePlayReviewService(config)

    private var googlePlayReviewConverter = GooglePlayReviewConverter()

    fun reviews(
        appId: String,
        language: GooglePlayLanguage = GooglePlayLanguage.ENGLISH_US,
        sortType: GooglePlaySortType = GooglePlaySortType.RECENT,
        batchSize: Int = 44
    ): Flow<GooglePlayReview.GooglePlayReview> = flow {

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
            delay(config.requestDelay)
            reviews.forEach { review ->
                emit(review)
            }
        }
    }
}
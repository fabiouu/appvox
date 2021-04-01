package dev.fabiou.appvox.core

import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.review.ReviewIterator
import dev.fabiou.appvox.core.review.ReviewRequest
import dev.fabiou.appvox.core.review.ReviewService
import dev.fabiou.appvox.core.review.itunesrss.ItunesRssReviewConverter
import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreSortType
import dev.fabiou.appvox.core.review.itunesrss.ItunesRssReviewService
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewRequest
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReview
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppStore(
    val config: RequestConfiguration = RequestConfiguration()
) {
//    private var appStoreReviewService = AppStoreReviewService(config)

    private var itunesRssReviewService = ItunesRssReviewService(config)

    private var itunesRssReviewConverter = ItunesRssReviewConverter()

    fun reviews(
        appId: String,
        region: String = "us",
        sortType: AppStoreSortType = AppStoreSortType.RELEVANT
    ): Flow<ItunesRssReview> = flow {

        val iterator = ReviewIterator(
            converter = itunesRssReviewConverter,
            service = itunesRssReviewService,
            request = ReviewRequest(
                ItunesRssReviewRequest(
                    appId = appId,
                    region = region,
                    sortType = sortType
                )
            )
        )

        iterator.forEach { reviews ->
            reviews.forEach { review ->
                delay(config.requestDelay)
                emit(review)
            }
        }
    }
}
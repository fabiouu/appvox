package dev.fabiou.appvox.core

import dev.fabiou.appvox.core.appstore.review.service.AppStoreReviewService
import dev.fabiou.appvox.core.configuration.Constant.MIN_REQUEST_DELAY
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.exception.AppVoxError
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.review.ReviewIterator
import dev.fabiou.appvox.core.review.ReviewRequest
import dev.fabiou.appvox.core.review.itunesrss.ItunesRssReviewConverter
import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreSortType
import dev.fabiou.appvox.core.review.itunesrss.ItunesRssReviewService
import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreRegion
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewRequest
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppStore(
    val config: RequestConfiguration = RequestConfiguration(requestDelay = MIN_REQUEST_DELAY)
) {
    private var appStoreReviewService = AppStoreReviewService(config)

    private var itunesRssReviewService = ItunesRssReviewService(config)

    private var itunesRssReviewConverter = ItunesRssReviewConverter()

    fun reviews(
        appId: String,
        region: AppStoreRegion = AppStoreRegion.ESTADOS_UNIDOS,
        sortType: AppStoreSortType = AppStoreSortType.RELEVANT
    ): Flow<ItunesRssReview> = flow {

        if (config.requestDelay < MIN_REQUEST_DELAY) {
            throw AppVoxException(AppVoxError.REQ_DELAY_TOO_SHORT)
        }

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
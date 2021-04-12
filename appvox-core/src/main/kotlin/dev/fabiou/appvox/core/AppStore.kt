package dev.fabiou.appvox.core

import dev.fabiou.appvox.core.configuration.Constant.MIN_REQUEST_DELAY
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.exception.AppVoxError
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.review.ReviewIterator
import dev.fabiou.appvox.core.review.ReviewRequest
import dev.fabiou.appvox.core.review.itunesrss.ItunesRssReviewConverter
import dev.fabiou.appvox.core.review.itunesrss.ItunesRssReviewService
import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreRegion
import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreSortType
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReview
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewRequest
import dev.fabiou.appvox.core.util.HttpUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppStore(
    val config: RequestConfiguration = RequestConfiguration(delay = MIN_REQUEST_DELAY)
) {
    private val itunesRssReviewService = ItunesRssReviewService(config)

    private val itunesRssReviewConverter = ItunesRssReviewConverter()

    init {
        if (config.proxy?.user != null && config.proxy.password != null) {
            HttpUtil.setAuthenticator(config.proxy.user, config.proxy.password)
        }
    }

    fun reviews(
        appId: String,
        region: AppStoreRegion = AppStoreRegion.UNITED_STATES,
        sortType: AppStoreSortType = AppStoreSortType.RELEVANT
    ): Flow<ItunesRssReview> = flow {

        if (config.delay < MIN_REQUEST_DELAY) {
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
                delay(config.delay.toLong())
                emit(review)
            }
        }
    }
}

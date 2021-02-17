package dev.fabiou.appvox.core.review.query

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.constant.AppStoreSortType
import dev.fabiou.appvox.core.review.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.review.constant.GooglePlaySortType
import dev.fabiou.appvox.core.review.domain.request.AppStoreReviewRequest
import dev.fabiou.appvox.core.review.domain.request.GooglePlayReviewRequest
import dev.fabiou.appvox.core.review.facade.AppStoreReviewFacade
import dev.fabiou.appvox.core.review.facade.GooglePlayReviewFacade
import dev.fabiou.appvox.core.review.iterator.AppStoreReviewIterator
import dev.fabiou.appvox.core.review.iterator.GooglePlayReviewIterator

class AppReview(
    private val configuration: dev.fabiou.appvox.core.configuration.Configuration = dev.fabiou.appvox.core.configuration.Configuration()
) {
    fun appStore(appId: String, sortType: AppStoreSortType, region: String, fetchCountLimit: Int = 0) : AppStoreReviewIterator {
        val facade = AppStoreReviewFacade(configuration)
        val request = AppStoreReviewRequest(
                region = region,
                sortType = sortType,
                countLimit = fetchCountLimit
        )
        return AppStoreReviewIterator(facade, appId, request)
    }

    fun googlePlay(
        appId: String,
        language: GooglePlayLanguage,
        sortType: GooglePlaySortType = GooglePlaySortType.RECENT,
        fetchCountLimit: Int = 0,
        batchSize: Int = 40) : GooglePlayReviewIterator {
        val facade = GooglePlayReviewFacade(configuration)
        val request = GooglePlayReviewRequest(
            language = language,
            sortType = sortType,
            fetchCountLimit = fetchCountLimit,
            batchSize = batchSize
        )
        return GooglePlayReviewIterator(facade, appId, request)
    }
}
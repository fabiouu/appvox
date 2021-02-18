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
    private val configuration: Configuration = Configuration()
) {
    private var appStoreFacade = AppStoreReviewFacade(configuration)

    fun appStore(appId: String, sortType: AppStoreSortType, region: String, fetchCountLimit: Int = 0) : AppStoreReviewIterator {

        val request = AppStoreReviewRequest(
                region = region,
                sortType = sortType,
                countLimit = fetchCountLimit
        )
        return AppStoreReviewIterator(appStoreFacade, appId, request)
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
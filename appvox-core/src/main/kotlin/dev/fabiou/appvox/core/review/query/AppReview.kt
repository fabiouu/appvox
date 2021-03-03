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
    configuration: Configuration = Configuration()
) {
    private var appStoreFacade = AppStoreReviewFacade(configuration)

    private var googlePlayFacade = GooglePlayReviewFacade(configuration)

    fun appStore(request: AppStoreReviewRequest) : AppStoreReviewIterator {
        return AppStoreReviewIterator(appStoreFacade, request)
    }

    fun googlePlay(request: GooglePlayReviewRequest) : GooglePlayReviewIterator {
        return GooglePlayReviewIterator(googlePlayFacade, request)
    }
}
package dev.fabiou.appvox.core.review.facade

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.domain.request.AppStoreReviewRequest
import dev.fabiou.appvox.core.review.domain.request.GooglePlayReviewRequest
import dev.fabiou.appvox.core.review.service.AppStoreReviewService
import dev.fabiou.appvox.core.review.service.GooglePlayReviewService
import dev.fabiou.appvox.core.review.iterator.AppStoreReviewIterator
import dev.fabiou.appvox.core.review.iterator.GooglePlayReviewIterator

class AppReview(
    configuration: Configuration = Configuration()
) {
    private var appStoreFacade = AppStoreReviewService(configuration)

    private var googlePlayFacade = GooglePlayReviewService(configuration)

    fun appStore(request: AppStoreReviewRequest) : AppStoreReviewIterator {
        return AppStoreReviewIterator(appStoreFacade, request)
    }

    fun googlePlay(request: GooglePlayReviewRequest) : GooglePlayReviewIterator {
        return GooglePlayReviewIterator(googlePlayFacade, request)
    }
}
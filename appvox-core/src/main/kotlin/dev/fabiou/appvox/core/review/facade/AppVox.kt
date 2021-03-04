package dev.fabiou.appvox.core.review.facade

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.domain.request.AppReviewRequest
import dev.fabiou.appvox.core.review.iterator.AppReviewIterator
import dev.fabiou.appvox.core.review.service.AppReviewService

class AppVox(
    configuration: Configuration = Configuration()
) {
    private var reviewService = AppReviewService(configuration)

    fun reviews(request: AppReviewRequest) : AppReviewIterator {
        return AppReviewIterator(reviewService, request)
    }
}
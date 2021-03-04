package dev.fabiou.appvox.core.review.service

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.domain.request.GooglePlayReviewRequest
import dev.fabiou.appvox.core.review.domain.response.AppReviewResponse
import dev.fabiou.appvox.core.review.repository.GooglePlayReviewRepository

class GooglePlayReviewService(
    val config : Configuration = Configuration()
) {
    private var reviewRepository = GooglePlayReviewRepository(config)

    fun getReviewsByAppId(request: GooglePlayReviewRequest) : AppReviewResponse {
        val reviews = reviewRepository.getReviewsByAppId(request)
        return reviews.toResponse()
    }
}
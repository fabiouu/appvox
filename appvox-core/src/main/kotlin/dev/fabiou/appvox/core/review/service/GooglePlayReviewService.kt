package dev.fabiou.appvox.core.review.service

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.domain.request.GooglePlayReviewRequest
import dev.fabiou.appvox.core.review.domain.request.ReviewRequest
import dev.fabiou.appvox.core.review.domain.response.ReviewResponse
import dev.fabiou.appvox.core.review.repository.GooglePlayReviewRepository

class GooglePlayReviewService(
    val config : Configuration = Configuration()
) {
    private var repository = GooglePlayReviewRepository(config)

    fun getReviewsByAppId(request: ReviewRequest, nextToken: String? = null) : ReviewResponse {
        val reviews = repository.getReviewsByAppId(request, nextToken)
        return reviews.toResponse()
    }
}
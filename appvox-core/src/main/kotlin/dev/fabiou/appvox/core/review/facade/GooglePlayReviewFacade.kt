package dev.fabiou.appvox.core.review.facade

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.converter.GooglePlayReviewConverter
import dev.fabiou.appvox.core.review.domain.request.GooglePlayReviewRequest
import dev.fabiou.appvox.core.review.domain.response.GooglePlayReviewResponse
import dev.fabiou.appvox.core.review.service.GooglePlayReviewService

class GooglePlayReviewFacade(
    val config : Configuration = Configuration()
) {
    private var service = GooglePlayReviewService(config)

    fun getReviewsByAppId(appId: String, request: GooglePlayReviewRequest) : GooglePlayReviewResponse {
        val reviews = service.getReviewsByAppId(appId = appId, request = request)
        return GooglePlayReviewConverter.toResponse(reviews)
    }
}
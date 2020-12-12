package com.appvox.core.review.facade

import com.appvox.core.configuration.Configuration
import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.converter.GooglePlayReviewConverter
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import com.appvox.core.review.domain.response.GooglePlayReviewResponse
import com.appvox.core.review.service.GooglePlayReviewService

class GooglePlayReviewFacade(
    val configuration : Configuration = Configuration()
) {
    fun getReviewsByAppId(appId: String, request: GooglePlayReviewRequest) : GooglePlayReviewResponse {
        val service = GooglePlayReviewService(configuration)
        val reviews = service.getReviewsByAppId(
                appId = appId,
                request = request
        )

        return GooglePlayReviewConverter.toResponse(reviews)
    }
}
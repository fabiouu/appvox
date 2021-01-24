package com.appvox.core.review.facade

import com.appvox.core.configuration.Configuration
import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.converter.GooglePlayReviewConverter
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import com.appvox.core.review.domain.response.GooglePlayReviewResponse
import com.appvox.core.review.service.GooglePlayReviewService

class GooglePlayReviewFacade(
    val config : Configuration = Configuration()
) {
    private var service = GooglePlayReviewService(config)

    fun getReviewsByAppId(appId: String, request: GooglePlayReviewRequest) : GooglePlayReviewResponse {
        val reviews = service.getReviewsByAppId(appId = appId, request = request)
        return GooglePlayReviewConverter.toResponse(reviews)
    }
}
package com.appvox.core.review.facade

import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.converter.GooglePlayReviewConverter
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import com.appvox.core.review.domain.response.GooglePlayReviewResponse
import com.appvox.core.review.service.GooglePlayReviewService

class GooglePlayReviewFacade(
    val configuration : ProxyConfiguration? = null
) {
    fun getReviewsByAppId(appId: String) : GooglePlayReviewResponse {
        val request = GooglePlayReviewRequest(
                language = "en",
                sortType = 1,
                size = 40
        )
        return getReviewsByAppId(appId, request)
    }

    fun getReviewsByAppId(appId: String, request: GooglePlayReviewRequest) : GooglePlayReviewResponse {
        val service = GooglePlayReviewService(configuration)
        val reviews = service.getReviewsByAppId(
                appId = appId,
                request = request
        )

        return GooglePlayReviewConverter.toResponse(reviews)
    }
}
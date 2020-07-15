package com.appvox.appvox.facade

import com.appvox.appvox.converter.GooglePlayReviewConverter
import com.appvox.appvox.domain.request.GooglePlayReviewRequest
import com.appvox.appvox.domain.response.ReviewsResponse
import com.appvox.appvox.service.GooglePlayReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GooglePlayReviewFacade(
        @Autowired
        private val googlePlayReviewService : GooglePlayReviewService,

        @Autowired
        private val googlePlayReviewConverter: GooglePlayReviewConverter
) {
    fun getReviewsByAppId(appId : String, request: GooglePlayReviewRequest) : ReviewsResponse {

        val reviews = googlePlayReviewService.getReviewsByAppId(
                appId = appId,
                language = request.language,
                sortType = request.sort,
                reviewCount = request.size,
                token = request.token)
        val reviewsResponse = googlePlayReviewConverter.toResponse(reviews)

        return reviewsResponse
    }
}
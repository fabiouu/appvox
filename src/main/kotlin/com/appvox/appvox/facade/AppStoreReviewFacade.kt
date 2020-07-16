package com.appvox.appvox.facade

import com.appvox.appvox.converter.AppStoreReviewConverter
import com.appvox.appvox.domain.request.AppStoreReviewRequest
import com.appvox.appvox.domain.response.ReviewsResponse
import com.appvox.appvox.service.AppStoreReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class AppStoreReviewFacade(
        @Autowired
        private val appStoreReviewService : AppStoreReviewService,

        @Autowired
        private val appStoreReviewConverter : AppStoreReviewConverter
) {
    fun getReviewsByAppId(appId : String, request: AppStoreReviewRequest) : ReviewsResponse {

        val reviews = appStoreReviewService.getReviewsByAppId(
                appId = appId,
                request = request
        )
        val reviewsResponse = appStoreReviewConverter.toResponse(reviews)

        return reviewsResponse
    }
}
package com.appvox.core.review.facade

import com.appvox.core.configuration.Configuration
import com.appvox.core.review.constant.AppStoreSortType
import com.appvox.core.review.converter.AppStoreReviewConverter
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.response.AppStoreReviewResponse
import com.appvox.core.review.service.AppStoreRecentReviewService
import com.appvox.core.review.service.AppStoreReviewService

class AppStoreReviewFacade(
        val config: Configuration
) {
    fun getReviewsByAppId(appId : String, request: AppStoreReviewRequest) : AppStoreReviewResponse {
        return if (request.sortType == AppStoreSortType.RECENT) {
            val reviewService = AppStoreRecentReviewService(config)
            var reviews = reviewService.getReviewsByAppId(appId = appId, request = request)
            AppStoreReviewConverter.toResponse(reviews)
        } else {
            val reviewService = AppStoreReviewService(config)
            if (request.bearerToken == null) {
                val bearerToken = reviewService.getBearerToken(appId, request.region)
                request.bearerToken = bearerToken
            }
            var reviews = reviewService.getReviewsByAppId(
                appId = appId,
                request = request
            )
            AppStoreReviewConverter.toResponse(reviews)
        }
    }
}
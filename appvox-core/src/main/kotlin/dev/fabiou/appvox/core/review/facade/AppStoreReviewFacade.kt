package dev.fabiou.appvox.core.review.facade

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.constant.AppStoreSortType
import dev.fabiou.appvox.core.review.converter.AppStoreReviewConverter
import dev.fabiou.appvox.core.review.domain.request.AppStoreReviewRequest
import dev.fabiou.appvox.core.review.domain.response.AppStoreReviewResponse
import dev.fabiou.appvox.core.review.service.AppStoreRecentReviewService
import dev.fabiou.appvox.core.review.service.AppStoreReviewService

class AppStoreReviewFacade(
        val config: Configuration
) {
    private var reviewService = AppStoreReviewService(config)

    fun getReviewsByAppId(appId : String, request: AppStoreReviewRequest) : AppStoreReviewResponse {
        return if (request.sortType == AppStoreSortType.RECENT) {
            var reviews = reviewService.getReviewsByAppId(appId = appId, request = request)
            AppStoreReviewConverter.toResponse(reviews)
        } else {
            if (request.bearerToken == null) {
                val bearerToken = reviewService.getBearerToken(appId, request.region)
                request.bearerToken = bearerToken
            }
            val reviews = reviewService.getReviewsByAppId(
                appId = appId,
                request = request
            )
            AppStoreReviewConverter.toResponse(reviews)
        }
    }
}
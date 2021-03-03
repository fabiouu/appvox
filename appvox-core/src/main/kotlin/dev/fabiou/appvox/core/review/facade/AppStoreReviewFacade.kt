package dev.fabiou.appvox.core.review.facade

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.constant.AppStoreSortType
import dev.fabiou.appvox.core.review.domain.request.AppStoreReviewRequest
import dev.fabiou.appvox.core.review.domain.response.ReviewResponse
import dev.fabiou.appvox.core.review.service.AppStoreRecentReviewService
import dev.fabiou.appvox.core.review.service.AppStoreReviewService
import dev.fabiou.appvox.core.translation.TranslationService

class AppStoreReviewFacade(
        val config: Configuration
) {
    private lateinit var translationService : TranslationService

    private var recentReviewService = AppStoreRecentReviewService(config)

    private var reviewService = AppStoreReviewService(config)

    init {
        try {
            val cls = Class.forName("dev.fabiou.appvox.core.translation.GoogleTranslationImpl")
            translationService = cls.newInstance() as TranslationService
        } catch (e: Exception) {

        }
    }

    fun getReviewsByAppId(request: AppStoreReviewRequest) : ReviewResponse {
        var response : ReviewResponse
        if (request.sortType == AppStoreSortType.RECENT) {
            var reviews = recentReviewService.getReviewsByAppId(request)
            response = reviews.toResponse()
        } else {
            if (request.bearerToken == null) {
                val bearerToken = reviewService.getBearerToken(request.appId, request.region)
                request.bearerToken = bearerToken
            }
            val reviews = reviewService.getReviewsByAppId(request)
            response = reviews.toResponse()
        }

        return response
    }
}
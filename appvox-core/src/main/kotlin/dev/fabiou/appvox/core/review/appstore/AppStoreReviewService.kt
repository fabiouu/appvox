package dev.fabiou.appvox.core.appstore.review.service

import dev.fabiou.appvox.core.app.appstore.AppStoreRepository
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.review.ReviewRequest
import dev.fabiou.appvox.core.review.ReviewResult
import dev.fabiou.appvox.core.review.ReviewService
import dev.fabiou.appvox.core.review.appstore.AppStoreReviewRepository
import dev.fabiou.appvox.core.review.appstore.domain.AppStoreReviewRequest
import dev.fabiou.appvox.core.review.appstore.domain.AppStoreReviewResult

internal class AppStoreReviewService(
    val config : RequestConfiguration = RequestConfiguration()
) : ReviewService<AppStoreReviewRequest, AppStoreReviewResult.AppStoreReview> {

    private var appStoreReviewRepository = AppStoreReviewRepository(config)

    private var appStoreRepository = AppStoreRepository(config)

    override fun getReviewsByAppId(request: ReviewRequest<AppStoreReviewRequest>) : ReviewResult<AppStoreReviewResult.AppStoreReview> {
        request.parameters.bearerToken = request.parameters.bearerToken ?: appStoreRepository.getBearerToken(request.parameters.appId, request.parameters.region)
        return appStoreReviewRepository.getReviewsByAppId(request)
    }
}
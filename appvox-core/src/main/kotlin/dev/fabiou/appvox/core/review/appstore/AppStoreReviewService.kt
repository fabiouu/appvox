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
    val config: RequestConfiguration
) : ReviewService<AppStoreReviewRequest, AppStoreReviewResult.AppStoreReview> {

    private val appStoreReviewRepository = AppStoreReviewRepository(config)

    private val appStoreRepository = AppStoreRepository(config)

    override fun getReviewsByAppId(
        request: ReviewRequest<AppStoreReviewRequest>
    ): ReviewResult<AppStoreReviewResult.AppStoreReview> {
        val bearerToken = appStoreRepository.getBearerToken(request.parameters.appId, request.parameters.region)
        val requestCopy = if (request.parameters.bearerToken == null) {
            request.copy(
                parameters = request.parameters.copy(
                    appId = request.parameters.appId,
                    region = request.parameters.region,
                    bearerToken = bearerToken
                ),
                nextToken = request.nextToken
            )
        } else {
            request.copy()
        }

        return appStoreReviewRepository.getReviewsByAppId(requestCopy)
    }
}

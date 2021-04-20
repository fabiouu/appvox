package dev.fabiou.appvox.review.googleplay

import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.ReviewResult
import dev.fabiou.appvox.review.ReviewService
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewRequest
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewResult

internal class GooglePlayReviewService(
    val config: RequestConfiguration
) : ReviewService<GooglePlayReviewRequest, GooglePlayReviewResult.GooglePlayReview> {

    private val googlePlayReviewRepository = GooglePlayReviewRepository(config)

    override fun getReviewsByAppId(
        request: ReviewRequest<GooglePlayReviewRequest>
    ): ReviewResult<GooglePlayReviewResult.GooglePlayReview> {
        return googlePlayReviewRepository.getReviewsByAppId(request)
    }
}

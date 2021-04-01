package dev.fabiou.appvox.core.review.googleplay

import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.review.ReviewRequest
import dev.fabiou.appvox.core.review.ReviewResult
import dev.fabiou.appvox.core.review.ReviewService
import dev.fabiou.appvox.core.review.appstore.AppStoreReviewRepository
import dev.fabiou.appvox.core.review.googleplay.domain.GooglePlayReviewRequest
import dev.fabiou.appvox.core.review.googleplay.domain.GooglePlayReviewResult
import dev.fabiou.appvox.core.review.itunesrss.ItunesRssReviewRepository

internal class GooglePlayReviewService(
    val config : RequestConfiguration = RequestConfiguration()
) : ReviewService<GooglePlayReviewRequest, GooglePlayReviewResult.GooglePlayReview> {
    private var googlePlayReviewRepository = GooglePlayReviewRepository(config)

    override fun getReviewsByAppId(request: ReviewRequest<GooglePlayReviewRequest>) : ReviewResult<GooglePlayReviewResult.GooglePlayReview> {
        return googlePlayReviewRepository.getReviewsByAppId(request)
    }
}
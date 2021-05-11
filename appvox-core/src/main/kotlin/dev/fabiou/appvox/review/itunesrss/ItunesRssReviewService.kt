package dev.fabiou.appvox.review.itunesrss

import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.ReviewResult
import dev.fabiou.appvox.review.ReviewService
import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReviewRequestParameters
import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReviewResult

internal class ItunesRssReviewService(
    val config: RequestConfiguration
) : ReviewService<ItunesRssReviewRequestParameters, ItunesRssReviewResult.Entry> {

    private val itunesRssReviewRepository = ItunesRssReviewRepository(config)

    override suspend fun getReviewsByAppId(
        request: ReviewRequest<ItunesRssReviewRequestParameters>
    ): ReviewResult<ItunesRssReviewResult.Entry> {
        return itunesRssReviewRepository.getReviewsByAppId(request)
    }
}

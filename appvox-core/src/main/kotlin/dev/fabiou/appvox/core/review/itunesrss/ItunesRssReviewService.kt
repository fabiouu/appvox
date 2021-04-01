package dev.fabiou.appvox.core.review.itunesrss

import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.review.ReviewIterator
import dev.fabiou.appvox.core.review.ReviewRequest
import dev.fabiou.appvox.core.review.ReviewResult
import dev.fabiou.appvox.core.review.ReviewService
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewRequest
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewResult

internal class ItunesRssReviewService(
    val config : RequestConfiguration = RequestConfiguration()
) : ReviewService<ItunesRssReviewRequest, ItunesRssReviewResult.Entry> {

    private var itunesRssReviewRepository = ItunesRssReviewRepository(config)

    override fun getReviewsByAppId(request: ReviewRequest<ItunesRssReviewRequest>): ReviewResult<ItunesRssReviewResult.Entry> {
        return itunesRssReviewRepository.getReviewsByAppId(request)
    }

//    override fun createIterator(request: ItunesRssReviewRequest) : ItunesRssReviewIterator {
////        return ItunesRssReviewIterator(this, request)
//    }
}
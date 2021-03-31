package dev.fabiou.appvox.core.review.itunesrss

import dev.fabiou.appvox.core.appstore.review.iterator.ItunesRssReviewListIterator
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.review.ReviewResult
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewRequest
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewResult

internal class ItunesRssReviewService(
    val config : RequestConfiguration = RequestConfiguration()
) {
    private var itunesRssReviewRepository = ItunesRssReviewRepository(config)

    fun getReviewsByAppId(request: ItunesRssReviewRequest) : ReviewResult<ItunesRssReviewResult> {
        return itunesRssReviewRepository.getReviewsByAppId(request)
    }

    fun createIterator(request: ItunesRssReviewRequest) : ItunesRssReviewIterator {
        return ItunesRssReviewIterator(this, request)
    }

    fun createListIterator(request: ItunesRssReviewRequest) : ItunesRssReviewListIterator {
        return ItunesRssReviewListIterator(this, request)
    }
}
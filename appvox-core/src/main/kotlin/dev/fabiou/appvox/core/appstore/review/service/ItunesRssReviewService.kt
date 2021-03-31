package dev.fabiou.appvox.core.appstore.review.service

import dev.fabiou.appvox.core.appstore.app.AppStoreRepository
import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewRequest
import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewResult
import dev.fabiou.appvox.core.appstore.review.domain.ItunesRssReviewRequest
import dev.fabiou.appvox.core.appstore.review.domain.ItunesRssReviewResult
import dev.fabiou.appvox.core.appstore.review.iterator.ItunesRssReviewIterator
import dev.fabiou.appvox.core.appstore.review.iterator.ItunesRssReviewListIterator
import dev.fabiou.appvox.core.appstore.review.repository.AppStoreReviewRepository
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.appstore.review.repository.ItunesRssReviewRepository
import dev.fabiou.appvox.core.common.ReviewResult

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
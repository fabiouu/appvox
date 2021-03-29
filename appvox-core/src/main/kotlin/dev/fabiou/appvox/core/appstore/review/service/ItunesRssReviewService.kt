package dev.fabiou.appvox.core.appstore.review.service

import dev.fabiou.appvox.core.appstore.app.repository.AppStoreRepository
import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewRequest
import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewResult
import dev.fabiou.appvox.core.appstore.review.domain.ItunesRssReviewRequest
import dev.fabiou.appvox.core.appstore.review.domain.ItunesRssReviewResult
import dev.fabiou.appvox.core.appstore.review.iterator.ItunesRssReviewIterator
import dev.fabiou.appvox.core.appstore.review.repository.AppStoreReviewRepository
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.appstore.review.repository.ItunesRssReviewRepository

class ItunesRssReviewService(
    val config : RequestConfiguration = RequestConfiguration()
) {
    private var itunesRssReviewRepository = ItunesRssReviewRepository(config)

    fun getReviewsByAppId(request: ItunesRssReviewRequest) : ItunesRssReviewResult {
        return itunesRssReviewRepository.getReviewsByAppId(request)
    }

    fun createIterator(request: ItunesRssReviewRequest) : ItunesRssReviewIterator {
        return ItunesRssReviewIterator(this, request)
    }
}
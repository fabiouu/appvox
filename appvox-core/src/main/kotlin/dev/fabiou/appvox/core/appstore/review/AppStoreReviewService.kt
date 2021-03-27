package dev.fabiou.appvox.core.appstore.review

import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.appstore.review.AppStoreReviewRequest
import dev.fabiou.appvox.core.appstore.review.ItunesRssReviewRequest
import dev.fabiou.appvox.core.appstore.review.AppStoreReviewResult
import dev.fabiou.appvox.core.appstore.review.ItunesRssReviewResult
import dev.fabiou.appvox.core.appstore.review.AppStoreReviewRepository
import dev.fabiou.appvox.core.appstore.review.ItunesRssReviewRepository

class AppStoreReviewService(
    val config : RequestConfiguration = RequestConfiguration()
) {
    private var itunesRssReviewRepository = ItunesRssReviewRepository(config)

    private var appStoreReviewRepository = AppStoreReviewRepository(config)

    fun getReviewsByAppId(request: ItunesRssReviewRequest) : ItunesRssReviewResult {
        return itunesRssReviewRepository.getReviewsByAppId(request)
    }

    fun getReviewsByAppId(request: AppStoreReviewRequest) : AppStoreReviewResult {
        request.bearerToken = request.bearerToken ?: appStoreReviewRepository.getBearerToken(request.appId, request.region)
        return appStoreReviewRepository.getReviewsByAppId(request)
    }

//    fun createIterator(request: AppReviewRequest) : AppReviewIterator {
//        return AppReviewIterator(this, request)
//    }
}
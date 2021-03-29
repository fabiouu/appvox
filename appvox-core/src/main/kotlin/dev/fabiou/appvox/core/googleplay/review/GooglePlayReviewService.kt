package dev.fabiou.appvox.core.googleplay.review

import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.appstore.review.repository.AppStoreReviewRepository
import dev.fabiou.appvox.core.googleplay.review.GooglePlayReviewRepository
import dev.fabiou.appvox.core.appstore.review.repository.ItunesRssReviewRepository
import dev.fabiou.appvox.core.googleplay.review.domain.GooglePlayReviewRequest
import dev.fabiou.appvox.core.googleplay.review.domain.GooglePlayReviewResult

class GooglePlayReviewService(
    val config : RequestConfiguration = RequestConfiguration()
) {
    private var googlePlayReviewRepository = GooglePlayReviewRepository(config)

    private var itunesRssReviewRepository = ItunesRssReviewRepository(config)

    private var appStoreReviewRepository = AppStoreReviewRepository(config)

//    fun getReviewsByAppId(request: AppReviewRequest, nextToken: String? = null) : AppReviewResponse {
//        return AppReviewResponse(ArrayList())
////        return itunesRssReviewRepository.getReviewsByAppId(request)
//    }

//    fun getReviewsByAppId(request: ItunesRssReviewRequest) : ItunesRssReviewResult {
//        return itunesRssReviewRepository.getReviewsByAppId(request)
//    }
//
//    fun getReviewsByAppId(request: AppStoreReviewRequest) : AppStoreReviewResult {
//        request.bearerToken = request.bearerToken ?: appStoreReviewRepository.getBearerToken(request.appId, request.region)
//        return appStoreReviewRepository.getReviewsByAppId(request)
//    }
//
    fun getReviewsByAppId(request: GooglePlayReviewRequest) : GooglePlayReviewResult {
        return googlePlayReviewRepository.getReviewsByAppId(request)
    }

    fun createIterator(request: GooglePlayReviewRequest) : GooglePlayReviewIterator {
        return GooglePlayReviewIterator(this, request)
    }
}
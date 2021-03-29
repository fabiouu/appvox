//package dev.fabiou.appvox.core.archive
//
//import dev.fabiou.appvox.core.configuration.RequestConfiguration
//import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewRequest
//import dev.fabiou.appvox.core.googleplay.review.domain.GooglePlayReviewRequest
//import dev.fabiou.appvox.core.appstore.review.domain.ItunesRssReviewRequest
//import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewResult
//import dev.fabiou.appvox.core.googleplay.review.domain.GooglePlayReviewResult
//import dev.fabiou.appvox.core.appstore.review.domain.ItunesRssReviewResult
//import dev.fabiou.appvox.core.googleplay.review.GooglePlayReviewIterator
//import dev.fabiou.appvox.core.appstore.app.repository.AppStoreRepository
//import dev.fabiou.appvox.core.appstore.review.repository.AppStoreReviewRepository
//import dev.fabiou.appvox.core.googleplay.review.GooglePlayReviewRepository
//import dev.fabiou.appvox.core.appstore.review.repository.ItunesRssReviewRepository
//
//class AppReviewService(
//    val config : RequestConfiguration = RequestConfiguration()
//) {
//    private var googlePlayReviewRepository = GooglePlayReviewRepository(config)
//
//    private var itunesRssReviewRepository = ItunesRssReviewRepository(config)
//
//    private var appStoreReviewRepository = AppStoreReviewRepository(config)
//
//    private var appStoreRepository = AppStoreRepository(config)
//
//    fun getReviewsByAppId(request: AppReviewRequest, nextToken: String? = null) : AppReviewResponse {
//        return AppReviewResponse(ArrayList(), nextToken)
////        return itunesRssReviewRepository.getReviewsByAppId(request)
//    }
//
//    fun getReviewsByAppId(request: ItunesRssReviewRequest) : ItunesRssReviewResult {
//        return itunesRssReviewRepository.getReviewsByAppId(request)
//    }
//
//    fun getReviewsByAppId(request: AppStoreReviewRequest) : AppStoreReviewResult {
//        request.bearerToken = request.bearerToken ?: appStoreRepository.getBearerToken(request.appId, request.region)
//        return appStoreReviewRepository.getReviewsByAppId(request)
//    }
//
//    fun getReviewsByAppId(request: GooglePlayReviewRequest) : GooglePlayReviewResult {
//        return googlePlayReviewRepository.getReviewsByAppId(request)
//    }
//
//    fun createIterator(request: AppReviewRequest) : GooglePlayReviewIterator {
//        return GooglePlayReviewIterator(this, request)
//    }
//}
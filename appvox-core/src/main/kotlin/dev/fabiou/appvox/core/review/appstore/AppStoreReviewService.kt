//package dev.fabiou.appvox.core.appstore.review.service
//
//import dev.fabiou.appvox.core.appstore.app.repository.AppStoreRepository
//import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewRequest
//import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewResult
//import dev.fabiou.appvox.core.appstore.review.domain.ItunesRssReviewRequest
//import dev.fabiou.appvox.core.appstore.review.domain.ItunesRssReviewResult
//import dev.fabiou.appvox.core.appstore.review.iterator.AppStoreReviewIterator
//import dev.fabiou.appvox.core.appstore.review.repository.AppStoreReviewRepository
//import dev.fabiou.appvox.core.configuration.RequestConfiguration
//import dev.fabiou.appvox.core.appstore.review.repository.ItunesRssReviewRepository
//import dev.fabiou.appvox.core.review.ReviewResult
//
//class AppStoreReviewService(
//    val config : RequestConfiguration = RequestConfiguration()
//) {
////    private var itunesRssReviewRepository = ItunesRssReviewRepository(config)
//
//    private var appStoreReviewRepository = AppStoreReviewRepository(config)
//
//    private var appStoreRepository = AppStoreRepository(config)
//
////    fun getReviewsByAppId(request: ItunesRssReviewRequest) : ItunesRssReviewResult {
////        return itunesRssReviewRepository.getReviewsByAppId(request)
////    }
//
//    fun getReviewsByAppId(request: AppStoreReviewRequest) : ReviewResult<AppStoreReviewResult> {
//        request.bearerToken = request.bearerToken ?: appStoreRepository.getBearerToken(request.appId, request.region)
//        return appStoreReviewRepository.getReviewsByAppId(request)
//    }
//
//    fun createIterator(request: AppStoreReviewRequest) : AppStoreReviewIterator {
//        return AppStoreReviewIterator(this, request)
//    }
//}
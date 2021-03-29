package dev.fabiou.appvox.core.appstore

import dev.fabiou.appvox.core.appstore.review.constant.AppStoreSortType
import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewRequest
import dev.fabiou.appvox.core.appstore.review.domain.ItunesRssReviewRequest
import dev.fabiou.appvox.core.appstore.review.iterator.AppStoreReviewIterator
import dev.fabiou.appvox.core.appstore.review.service.AppStoreReviewService
import dev.fabiou.appvox.core.appstore.review.service.ItunesRssReviewService
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.googleplay.review.GooglePlayReviewIterator

class AppStore(
    config: RequestConfiguration = RequestConfiguration()
) {
    private var appStoreReviewService = AppStoreReviewService(config)

    private var itunesRssReviewService = ItunesRssReviewService(config)

    fun reviews(appId : String,
                region : String = "us",
                sortType : AppStoreSortType = AppStoreSortType.RELEVANT,
                maxCount : Int = Int.MAX_VALUE) : Any/*AppStoreReviewIterator*/ {

        val request = ItunesRssReviewRequest(
                appId = appId,
                region = region,
                sortType = sortType,
                maxCount = maxCount)

        return itunesRssReviewService.createIterator(request)
    }

//    fun reviews(appId : String,
//                region : String = "us",
//                sortType : AppStoreSortType = AppStoreSortType.RELEVANT,
//                maxCount : Int = Int.MAX_VALUE,
//    isAll : Boolean) : AppStoreReviewIterator {
//
//        val request = AppStoreReviewRequest(
//                appId = appId,
//                region = region,
//                sortType = sortType,
//                maxCount = maxCount)
//
//        return itunesRssReviewService.createIterator(request)
//    }


    fun reviews(request: AppStoreReviewRequest) : Any {
        return appStoreReviewService.createIterator(request)
    }

//    val appId: String,
//    val region: String,
//    val language: GooglePlayLanguage = GooglePlayLanguage.ENGLISH_US,
//    val sortType: AppReviewSortType = AppReviewSortType.RECENT,
//    val maxCount: Int = Int.MAX_VALUE
//    fun reviews(val appId: String, val region: String, )
}
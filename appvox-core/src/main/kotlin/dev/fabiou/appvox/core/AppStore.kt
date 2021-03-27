package dev.fabiou.appvox.core

import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.archive.AppReviewRequest
import dev.fabiou.appvox.core.googleplay.review.GooglePlayReviewIterator
import dev.fabiou.appvox.core.archive.AppReviewService

class AppStore(
    config: RequestConfiguration = RequestConfiguration()
) {
    private var reviewService = AppReviewService(config)

    fun reviews(request: AppReviewRequest) : GooglePlayReviewIterator {
        return reviewService.createIterator(request)
    }

//    val appId: String,
//    val region: String,
//    val language: GooglePlayLanguage = GooglePlayLanguage.ENGLISH_US,
//    val sortType: AppReviewSortType = AppReviewSortType.RECENT,
//    val maxCount: Int = Int.MAX_VALUE
//    fun reviews(val appId: String, val region: String, )
}
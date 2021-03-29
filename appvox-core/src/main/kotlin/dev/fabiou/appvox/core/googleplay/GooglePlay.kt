package dev.fabiou.appvox.core.googleplay

import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.googleplay.review.GooglePlayReviewIterator
import dev.fabiou.appvox.core.googleplay.review.GooglePlayReviewService
import dev.fabiou.appvox.core.googleplay.review.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.googleplay.review.constant.GooglePlaySortType
import dev.fabiou.appvox.core.googleplay.review.domain.GooglePlayReviewRequest

class GooglePlay(
        config: RequestConfiguration = RequestConfiguration()
) {
    private var reviewService = GooglePlayReviewService(config)

    fun reviews(appId: String,
                language: GooglePlayLanguage = GooglePlayLanguage.ENGLISH_US,
                sortType: GooglePlaySortType = GooglePlaySortType.RECENT,
                maxCount: Int = Int.MAX_VALUE): GooglePlayReviewIterator {

        val request = GooglePlayReviewRequest(
                appId = appId,
                language = language,
                sortType = sortType,
                maxCount = maxCount,
                batchSize = 100
        )

        return reviewService.createIterator(request)
    }

//    fun reviews(request: GooglePlayReviewRequest) : GooglePlayReviewIterator {
//        return reviewService.createIterator(request)
//    }

//    fun reviews(request: GooglePlayReviewRequest) : Any {
//        return reviewService.createIterator(request)
//    }

//    val appId: String,
//    val region: String,
//    val language: GooglePlayLanguage = GooglePlayLanguage.ENGLISH_US,
//    val sortType: AppReviewSortType = AppReviewSortType.RECENT,
//    val maxCount: Int = Int.MAX_VALUE
//    fun reviews(val appId: String, val region: String, )
}
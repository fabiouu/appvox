package dev.fabiou.appvox.core.review.domain.request

import dev.fabiou.appvox.core.review.constant.AppReviewSortType
import dev.fabiou.appvox.core.review.constant.GooglePlayLanguage

open class ReviewRequest(
        val appId: String,
        val region: String,
        val language: GooglePlayLanguage = GooglePlayLanguage.ENGLISH_US,
        val sortType: AppReviewSortType = AppReviewSortType.RECENT,
        val maxCount: Int = Int.MAX_VALUE,
        val batchSize: Int = 100,
)
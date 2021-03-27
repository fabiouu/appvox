package dev.fabiou.appvox.core.archive

import dev.fabiou.appvox.core.googleplay.review.constant.AppReviewSortType
import dev.fabiou.appvox.core.googleplay.review.constant.GooglePlayLanguage

open class AppReviewRequest(
    val appId: String,
    val region: String,
    val language: GooglePlayLanguage = GooglePlayLanguage.ENGLISH_US,
    val sortType: AppReviewSortType = AppReviewSortType.RECENT,
    val maxCount: Int = Int.MAX_VALUE
)
package dev.fabiou.appvox.core.review.domain.request

import dev.fabiou.appvox.core.review.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.review.constant.GooglePlaySortType
import kotlin.Int.Companion.MAX_VALUE

open class GooglePlayReviewRequest(
    val appId: String,
    val language: GooglePlayLanguage = GooglePlayLanguage.ENGLISH_US,
    val sortType: GooglePlaySortType = GooglePlaySortType.RECENT,
    val batchSize: Int = 100,
    val maxCount: Int = MAX_VALUE,
    var nextToken: String? = null
)
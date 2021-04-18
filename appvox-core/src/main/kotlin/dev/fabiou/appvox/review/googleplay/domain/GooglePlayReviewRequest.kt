package dev.fabiou.appvox.review.googleplay.domain

import dev.fabiou.appvox.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.review.googleplay.constant.GooglePlaySortType

class GooglePlayReviewRequest(
    val appId: String,
    val nextToken: String? = null,
    val language: GooglePlayLanguage,
    val sortType: GooglePlaySortType,
    val batchSize: Int,
)

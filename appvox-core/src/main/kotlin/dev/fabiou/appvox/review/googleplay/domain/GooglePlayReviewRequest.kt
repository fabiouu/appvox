package dev.fabiou.appvox.review.googleplay.domain

import dev.fabiou.appvox.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.review.googleplay.constant.GooglePlaySortType

internal data class GooglePlayReviewRequest(
    val appId: String,
    val language: GooglePlayLanguage,
    val sortType: GooglePlaySortType,
    val batchSize: Int,
    val sid: String,
    val bl: String,
    val at: String
)

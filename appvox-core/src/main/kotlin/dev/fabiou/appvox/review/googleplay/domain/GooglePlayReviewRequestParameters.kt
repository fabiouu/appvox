package dev.fabiou.appvox.review.googleplay.domain

import dev.fabiou.appvox.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.review.googleplay.constant.GooglePlaySortType

internal data class GooglePlayReviewRequestParameters(
    val appId: String,
    val language: GooglePlayLanguage,
    val sortType: GooglePlaySortType,
    val rating: Int? = null,
    val fetchHistory: Boolean,
    val batchSize: Int,
    val deviceName: String? = null,
    val sid: String? = null,
    val bl: String? = null
)

package dev.fabiou.appvox.review.googleplay.domain

import dev.fabiou.appvox.review.googleplay.constant.GooglePlayLanguage

internal data class GooglePlayReviewHistoryRequestParameters(
    val appId: String,
    val reviewId: String,
    val language: GooglePlayLanguage,
    val batchSize: Int,
    val sid: String? = null,
    val bl: String? = null
)

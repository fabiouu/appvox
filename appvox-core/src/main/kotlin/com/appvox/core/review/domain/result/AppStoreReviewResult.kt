package com.appvox.core.review.domain.result

data class AppStoreReviewResult(
        val id: String,
        val type: String,
        val attributes: AppStoreReviewAttributes
)
package com.appvox.core.domain.result.appstore

data class AppStoreReviewResult(
        val id: String,
        val type: String,
        val attributes: AppStoreReviewAttributes
)
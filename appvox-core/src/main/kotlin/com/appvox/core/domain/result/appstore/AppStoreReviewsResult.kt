package com.appvox.core.domain.result.appstore

data class AppStoreReviewsResult(
        val next: String?,
        val data: List<AppStoreReviewResult>
)
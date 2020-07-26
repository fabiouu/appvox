package com.appvox.core.review.domain.result

data class AppStoreReviewsResult(
        val next: String?,
        val data: List<AppStoreReviewResult>
)
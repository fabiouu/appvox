package com.appvox.appvox.domain.request

data class AppStoreReviewRequest(
    val region: String = "us",
    val size: Int = 0
)
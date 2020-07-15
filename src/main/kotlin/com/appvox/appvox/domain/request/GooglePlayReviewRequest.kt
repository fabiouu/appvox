package com.appvox.appvox.domain.request

data class GooglePlayReviewRequest(
    val language: String = "en",
    val sort: Int = 1,
    val size: Int = 40,
    val token: String? = ""
)
package com.appvox.appvox.domain.response

data class ReviewsResponse(
    val next: String?,
    val reviews: List<ReviewResponse>
)
package com.appvox.core.review.domain.result

data class GooglePlayReviewsResult(var token: String?, val reviews: List<GooglePlayReviewResult>)
package com.appvox.appvox.domain.result.appstore

import com.appvox.appvox.domain.result.googleplay.GooglePlayReviewResult
import com.fasterxml.jackson.annotation.JsonProperty

data class AppStoreReviewsResult(
        val next: String?,

        @JsonProperty("data")
        val appStoreReviews: List<AppStoreReviewResult>
)
package com.appvox.appvox.domain.result.appstore

import com.fasterxml.jackson.annotation.JsonProperty

data class AppStoreReviewResult(
        val id: String,
        val type: String,
        val attributes: AppStoreReviewAttributes
)
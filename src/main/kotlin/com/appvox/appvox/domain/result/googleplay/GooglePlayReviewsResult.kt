package com.appvox.appvox.domain.result.googleplay

import com.appvox.appvox.domain.result.googleplay.GooglePlayReviewResult

data class GooglePlayReviewsResult(val token: String?, val googlePlayReviews: List<GooglePlayReviewResult>) {
}
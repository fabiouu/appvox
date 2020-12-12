package com.appvox.core.review.domain.response

data class AppStoreReviewResponse(
        val reviews: List<AppStoreReview>,
        val nextToken: String?
) {
    data class AppStoreReview(
            val type: String,
            val id: String,
            val userName: String,
            val rating: Int,
            val title: String? = null,
            val comment: String,
            val commentTime: Long? = 0,
            val replyComment: String? = null,
            val replyTime: Long? = 0,
            val url: String? = null
    )
}
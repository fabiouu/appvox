package com.appvox.core.review.domain.response

data class AppStoreReviewResponse(
        val reviews: List<AppStoreReview>,
        val nextToken: String?
) {
    data class AppStoreReview(
        val type: String,
        val id: String,
        val userName: String,
        val userProfile: String? = null,
        val rating: Int,
        val title: String? = null,
        val comment: String,
        val commentTime: Long? = 0,
        val replyComment: String? = null,
        val replyCommentTime: Long? = 0,
        val likeCount: Int? = 0,
        val appVersion: String? = null,
        val url: String? = null
    )
}
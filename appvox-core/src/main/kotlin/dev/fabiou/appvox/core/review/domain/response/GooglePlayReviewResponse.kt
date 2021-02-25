package dev.fabiou.appvox.core.review.domain.response

data class GooglePlayReviewResponse(
        val reviews: List<GooglePlayReview>,
        val nextToken: String?
) {
    data class GooglePlayReview(
        val id: String,
        val userName: String,
        val userAvatar: String? = null,
        val rating: Int,
        val title: String? = null,
        val appVersion: String? = null,
        val comment: String,
        var translatedComment: String? = null,
        val commentTime: Long? = 0,
        val replyComment: String? = null,
        val replyTime: Long? = 0,
        val likeCount: Int? = 0,
        val url: String? = null
    )
}
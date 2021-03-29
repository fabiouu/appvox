package dev.fabiou.appvox.core.googleplay.review.domain

data class GooglePlayReviewResult(
    val reviews: List<GooglePlayReview>,
    var nextToken: String? = null
) {
    data class GooglePlayReview(
        val reviewId: String,
        val userName: String,
        val userProfilePicUrl: String,
        val rating: Int,
        val comment: String,
        val submitTime: Long,
        val likeCount: Int,
        val appVersion: String?,
        val reviewUrl: String,
        val replyComment: String?,
        val replySubmitTime: Long?
    )
}
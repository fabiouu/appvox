package dev.fabiou.appvox.review.googleplay.domain

data class GooglePlayReviewResult(
    val reviews: List<GooglePlayReview>
) {
    data class GooglePlayReview(
        val reviewId: String,
        val userName: String,
        val userProfilePicUrl: String,
        val rating: Int,
        val comment: String,
        val submitTime: Long,
        val likeCount: Int,
        val reviewUrl: String,
        val appVersion: String?,
        val replyComment: String?,
        val replySubmitTime: Long?
    )
}

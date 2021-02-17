package dev.fabiou.appvox.core.review.domain.result

data class GooglePlayReviewResult(
        var token: String?,
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
            val appVersion: String?,
            val reviewUrl: String,
            val replyComment: String?,
            val replySubmitTime: Long?
    )
}
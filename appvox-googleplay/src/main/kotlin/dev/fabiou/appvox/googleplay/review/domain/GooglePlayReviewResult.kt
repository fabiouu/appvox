package dev.fabiou.appvox.googleplay.review.domain

data class GooglePlayReviewResult(
    val reviewId: String,
    val userName: String,
    val userProfilePicUrl: String,
    val rating: Int,
    val userCommentText: String,
    val userCommentTime: Long,
    val likeCount: Int,
    val reviewUrl: String,
    val appVersion: String?,
    val criterias: List<Criteria>,
    val hasEditHistory: Boolean?,
    val developerCommentText: String?,
    val developerCommentTime: Long?
) {
    data class Criteria(
        val name: String,
        val rating: Int
    )
}

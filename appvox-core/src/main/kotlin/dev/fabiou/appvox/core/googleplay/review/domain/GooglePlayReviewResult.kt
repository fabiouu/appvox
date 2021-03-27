package dev.fabiou.appvox.core.googleplay.review.domain

internal data class GooglePlayReviewResult(
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

//    fun toResponse(): AppReviewResponse {
//        var reviews = ArrayList<AppReviewResponse.AppReview>()
//        val googlePlayReviews = this.reviews
//        for (googlePlayReview in googlePlayReviews) {
//            val reviewResponse = AppReviewResponse.AppReview(
//                id = googlePlayReview.reviewId,
//                userName = googlePlayReview.userName,
//                userAvatar = googlePlayReview.userProfilePicUrl,
//                rating = googlePlayReview.rating,
//                comment = googlePlayReview.comment,
//                commentTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(googlePlayReview.submitTime), ZoneOffset.UTC),
//                replyComment = googlePlayReview.replyComment,
////                      replyTime = if (googlePlayReview != null && googlePlayReview.replySubmitTime != 0) googlePlayReview.replySubmitTime?.let { ZonedDateTime.ofInstant(Instant.ofEpochSecond(googlePlayReview.replySubmitTime), ZoneOffset.UTC) } else null,
//                likeCount = googlePlayReview.likeCount,
//                appVersion = googlePlayReview.appVersion,
//                url = googlePlayReview.reviewUrl
//            )
//            reviews.add(reviewResponse)
//        }
//
//        return AppReviewResponse(
//            reviews = reviews,
//            nextToken = this.token
//        )
//    }
}
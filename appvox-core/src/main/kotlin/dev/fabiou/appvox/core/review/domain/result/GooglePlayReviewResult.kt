package dev.fabiou.appvox.core.review.domain.result

import dev.fabiou.appvox.core.review.domain.response.ReviewResponse
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

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

    fun toResponse(): ReviewResponse {
        var reviews = ArrayList<ReviewResponse.StoreReview>()
        val googlePlayReviews = this.reviews
        for (googlePlayReview in googlePlayReviews) {
            val reviewResponse = ReviewResponse.StoreReview(
                id = googlePlayReview.reviewId,
                userName = googlePlayReview.userName,
                userAvatar = googlePlayReview.userProfilePicUrl,
                rating = googlePlayReview.rating,
                comment = googlePlayReview.comment,
                commentTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(googlePlayReview.submitTime), ZoneOffset.UTC),
                replyComment = googlePlayReview.replyComment,
//                      replyTime = if (googlePlayReview != null && googlePlayReview.replySubmitTime != 0) googlePlayReview.replySubmitTime?.let { ZonedDateTime.ofInstant(Instant.ofEpochSecond(googlePlayReview.replySubmitTime), ZoneOffset.UTC) } else null,
                likeCount = googlePlayReview.likeCount,
                appVersion = googlePlayReview.appVersion,
                url = googlePlayReview.reviewUrl
            )
            reviews.add(reviewResponse)
        }

        return ReviewResponse(
            reviews = reviews,
            nextToken = this.token
        )
    }
}
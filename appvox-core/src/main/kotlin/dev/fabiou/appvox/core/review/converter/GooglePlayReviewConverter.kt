package dev.fabiou.appvox.core.review.converter

import dev.fabiou.appvox.core.review.domain.response.GooglePlayReviewResponse
import dev.fabiou.appvox.core.review.domain.result.GooglePlayReviewResult
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

class GooglePlayReviewConverter {
    companion object {
        fun toResponse(reviewResult: GooglePlayReviewResult): GooglePlayReviewResponse {
            var reviews = ArrayList<GooglePlayReviewResponse.GooglePlayReview>()
            val googlePlayReviews = reviewResult.reviews
            for (googlePlayReview in googlePlayReviews) {
                val reviewResponse = GooglePlayReviewResponse.GooglePlayReview(
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

            return GooglePlayReviewResponse(
                reviews = reviews,
                nextToken = reviewResult.token
            )
        }
    }
}
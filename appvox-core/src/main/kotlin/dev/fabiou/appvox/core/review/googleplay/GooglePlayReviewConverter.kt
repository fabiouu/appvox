package dev.fabiou.appvox.core.review.googleplay

import dev.fabiou.appvox.core.review.googleplay.domain.GooglePlayReviewResponse
import dev.fabiou.appvox.core.review.googleplay.domain.GooglePlayReviewResult
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

class GooglePlayReviewConverter {
    companion object {
        fun toResponse(reviewResult: GooglePlayReviewResult.GooglePlayReview): GooglePlayReviewResponse.GooglePlayReview {
            val reviewResponse = GooglePlayReviewResponse.GooglePlayReview(
                id = reviewResult.reviewId,
                userName = reviewResult.userName,
                userAvatar = reviewResult.userProfilePicUrl,
                rating = reviewResult.rating,
                comment = reviewResult.comment,
                commentTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(reviewResult.submitTime), ZoneOffset.UTC),
                replyComment = reviewResult.replyComment,
//                      replyTime = if (googlePlayReview != null && googlePlayReview.replySubmitTime != 0) googlePlayReview.replySubmitTime?.let { ZonedDateTime.ofInstant(Instant.ofEpochSecond(googlePlayReview.replySubmitTime), ZoneOffset.UTC) } else null,
                likeCount = reviewResult.likeCount,
                appVersion = reviewResult.appVersion,
                url = reviewResult.reviewUrl
            )
            return reviewResponse
        }
    }
}
package dev.fabiou.appvox.review.googleplay

import dev.fabiou.appvox.review.ReviewConverter
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewResult
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

internal class GooglePlayReviewConverter :
    ReviewConverter<GooglePlayReviewResult.GooglePlayReview, GooglePlayReview> {

        override fun toResponse(
            results: List<GooglePlayReviewResult.GooglePlayReview>
        ): List<GooglePlayReview> {
            val response = ArrayList<GooglePlayReview>()
            results.forEach { reviewResult ->
                val reviewResponse = GooglePlayReview(
                    id = reviewResult.reviewId,
                    userName = reviewResult.userName,
                    userAvatar = reviewResult.userProfilePicUrl,
                    rating = reviewResult.rating,
                    comment = reviewResult.comment,
                    commentTime = ZonedDateTime.ofInstant(
                        Instant.ofEpochSecond(reviewResult.submitTime),
                        ZoneOffset.UTC
                    ),
                    replyComment = reviewResult.replyComment,
                    // TODO replyTime = if (googlePlayReview != null && googlePlayReview.replySubmitTime != 0) googlePlayReview.replySubmitTime?.let { ZonedDateTime.ofInstant(Instant.ofEpochSecond(googlePlayReview.replySubmitTime), ZoneOffset.UTC) } else null,
                    likeCount = reviewResult.likeCount,
                    appVersion = reviewResult.appVersion,
                    url = reviewResult.reviewUrl
                )
                response.add(reviewResponse)
            }
            return response
    }
}

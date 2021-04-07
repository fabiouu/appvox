package dev.fabiou.appvox.core.review.googleplay

import dev.fabiou.appvox.core.review.ReviewConverter
import dev.fabiou.appvox.core.review.googleplay.domain.GooglePlayReview
import dev.fabiou.appvox.core.review.googleplay.domain.GooglePlayReviewResult
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

internal class GooglePlayReviewConverter :
    ReviewConverter<GooglePlayReviewResult.GooglePlayReview, GooglePlayReview.GooglePlayReview> {

        override fun toResponse(
            reviewResults: List<GooglePlayReviewResult.GooglePlayReview>
        ): List<GooglePlayReview.GooglePlayReview> {
            val response = ArrayList<GooglePlayReview.GooglePlayReview>()
            for (reviewResult in reviewResults) {
                val reviewResponse = GooglePlayReview.GooglePlayReview(
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
//                      replyTime = if (googlePlayReview != null && googlePlayReview.replySubmitTime != 0) googlePlayReview.replySubmitTime?.let { ZonedDateTime.ofInstant(Instant.ofEpochSecond(googlePlayReview.replySubmitTime), ZoneOffset.UTC) } else null,
                    likeCount = reviewResult.likeCount,
                    appVersion = reviewResult.appVersion,
                    url = reviewResult.reviewUrl
                )
                response.add(reviewResponse)
            }
            return response
    }
}

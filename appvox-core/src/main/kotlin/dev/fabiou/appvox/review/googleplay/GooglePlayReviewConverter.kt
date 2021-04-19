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
        val reviews = ArrayList<GooglePlayReview>()
        results.forEach { result ->
            val review = GooglePlayReview(
                id = result.reviewId,
                userName = result.userName,
                userAvatar = result.userProfilePicUrl,
                rating = result.rating,
                comment = result.comment,
                commentTime = ZonedDateTime.ofInstant(
                    Instant.ofEpochSecond(result.submitTime),
                    ZoneOffset.UTC
                ),
                replyComment = result.replyComment,
                replyTime = result.replySubmitTime?.let {
                    ZonedDateTime.ofInstant(Instant.ofEpochSecond(it), ZoneOffset.UTC)
                },
                likeCount = result.likeCount,
                appVersion = result.appVersion,
                url = result.reviewUrl
            )
            reviews.add(review)
        }
        return reviews
    }
}

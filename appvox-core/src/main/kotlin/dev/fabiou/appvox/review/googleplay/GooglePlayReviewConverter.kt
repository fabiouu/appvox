package dev.fabiou.appvox.review.googleplay

import dev.fabiou.appvox.review.ReviewConverter
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview.DeveloperComment
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview.UserComment
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewResult
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

internal class GooglePlayReviewConverter :
    ReviewConverter<GooglePlayReviewResult, GooglePlayReview> {

    override fun toResponse(
        results: List<GooglePlayReviewResult>
    ): List<GooglePlayReview> {
        val reviews = ArrayList<GooglePlayReview>()
        results.forEach { result ->
            val review = GooglePlayReview(
                id = result.reviewId,
                url = result.reviewUrl,
                comments = arrayListOf(
                    GooglePlayReview.Comment(
                        user = UserComment(
                            userName = result.userName,
                            userAvatar = result.userProfilePicUrl,
                            rating = result.rating,
                            text = result.userCommentText,
                            lastUpdateTime = ZonedDateTime.ofInstant(
                                Instant.ofEpochSecond(result.userCommentTime),
                                ZoneOffset.UTC
                            ),
                            likeCount = result.likeCount,
                            appVersion = result.appVersion,
                        ),
                        developer = DeveloperComment(
                            text = result.developerCommentText,
                            lastUpdateTime = result.developerCommentTime?.let {
                                ZonedDateTime.ofInstant(Instant.ofEpochSecond(it), ZoneOffset.UTC)
                            },
                        )
                    )
                )
            )
            reviews.add(review)
        }
        return reviews
    }
}

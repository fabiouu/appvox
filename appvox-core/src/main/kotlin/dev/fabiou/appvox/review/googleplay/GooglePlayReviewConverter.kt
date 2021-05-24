package dev.fabiou.appvox.review.googleplay

import dev.fabiou.appvox.review.classification.UserPersona
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview.DeveloperComment
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview.UserComment
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewResult
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

internal class GooglePlayReviewConverter {

    fun toResponse(
        result: GooglePlayReviewResult
    ): GooglePlayReview {
        return GooglePlayReview(
            id = result.reviewId,
            url = result.reviewUrl,
            userTypes = emptySet<UserPersona>(),
            comments = arrayListOf(
                GooglePlayReview.Comment(
                    user = UserComment(
                        name = result.userName,
                        avatar = result.userProfilePicUrl,
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
    }

    fun toResponseWithHistory(
        result: GooglePlayReviewResult,
        reviewHistory: List<GooglePlayReviewResult>,
        userTypes: Set<UserPersona>
    ): GooglePlayReview {
        return GooglePlayReview(
            id = result.reviewId,
            url = result.reviewUrl,
            userTypes = userTypes,
            comments = reviewHistory.map {
                GooglePlayReview.Comment(
                    user = UserComment(
                        name = it.userName,
                        avatar = it.userProfilePicUrl,
                        rating = it.rating,
                        text = it.userCommentText,
                        lastUpdateTime = ZonedDateTime.ofInstant(
                            Instant.ofEpochSecond(it.userCommentTime),
                            ZoneOffset.UTC
                        ),
                        likeCount = it.likeCount,
                        appVersion = it.appVersion,
                    ),
                    developer = DeveloperComment(
                        text = it.developerCommentText,
                        lastUpdateTime = it.developerCommentTime?.let { developerCommentTime ->
                            ZonedDateTime.ofInstant(
                                Instant.ofEpochSecond(developerCommentTime),
                                ZoneOffset.UTC
                            )
                        },
                    )
                )
            }
        )
    }
}

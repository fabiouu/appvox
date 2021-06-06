package dev.fabiou.appvox.review.googleplay

import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview.DeveloperComment
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview.UserComment
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewRequestParameters
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewResult
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

internal class GooglePlayReviewConverter {

    fun toResponse(
        requestParameters: GooglePlayReviewRequestParameters,
        result: GooglePlayReviewResult
    ): GooglePlayReview {
        return GooglePlayReview(
            id = result.reviewId,
            language = requestParameters.language,
            url = result.reviewUrl,
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
                        appVersion = result.appVersion
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
        requestParameters: GooglePlayReviewRequestParameters,
        result: GooglePlayReviewResult,
        reviewHistory: List<GooglePlayReviewResult>
    ): GooglePlayReview {
        return GooglePlayReview(
            id = result.reviewId,
            language = requestParameters.language,
            url = result.reviewUrl,
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
                        likeCount = result.likeCount,
                        appVersion = it.appVersion
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

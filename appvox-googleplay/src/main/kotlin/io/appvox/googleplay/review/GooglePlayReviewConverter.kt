package io.appvox.googleplay.review

import io.appvox.googleplay.review.domain.GooglePlayReview
import io.appvox.googleplay.review.domain.GooglePlayReviewRequestParameters
import io.appvox.googleplay.review.domain.GooglePlayReviewResult
import java.time.Instant.ofEpochSecond
import java.time.ZoneOffset
import java.time.ZonedDateTime.ofInstant

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
                    user = GooglePlayReview.UserComment(
                        name = result.userName,
                        avatar = result.userProfilePicUrl,
                        rating = result.rating,
                        text = result.userCommentText,
                        lastUpdateTime = ofInstant(
                            ofEpochSecond(result.userCommentTime),
                            ZoneOffset.UTC
                        ),
                        likeCount = result.likeCount,
                        appVersion = result.appVersion
                    ),
                    developer = result.developerCommentText?.let {
                        GooglePlayReview.DeveloperComment(
                            text = result.developerCommentText,
                            lastUpdateTime = ofInstant(ofEpochSecond(result.developerCommentTime!!), ZoneOffset.UTC),
                        )
                    }
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
                    user = GooglePlayReview.UserComment(
                        name = it.userName,
                        avatar = it.userProfilePicUrl,
                        rating = it.rating,
                        text = it.userCommentText,
                        lastUpdateTime = ofInstant(
                            ofEpochSecond(it.userCommentTime),
                            ZoneOffset.UTC
                        ),
                        likeCount = result.likeCount,
                        appVersion = it.appVersion
                    ),
                    developer = result.developerCommentText?.let {
                        GooglePlayReview.DeveloperComment(
                            text = result.developerCommentText,
                            lastUpdateTime = ofInstant(ofEpochSecond(result.developerCommentTime!!), ZoneOffset.UTC),
                        )
                    }
                )
            }
        )
    }
}

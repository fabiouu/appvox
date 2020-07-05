package com.appvox.appvox.converter

import com.appvox.appvox.domain.response.ReviewResponse
import com.appvox.appvox.domain.response.ReviewsResponse
import com.appvox.appvox.domain.result.appstore.AppStoreReviewsResult
import com.appvox.appvox.domain.result.googleplay.GooglePlayReviewsResult
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant


@Component
class ReviewConverter {

    fun toResponse(reviewResult : GooglePlayReviewsResult) : ReviewsResponse {
        var reviews = ArrayList<ReviewResponse>()
        val googlePlayReviews = reviewResult.googlePlayReviews
        for (googlePlayReview in googlePlayReviews) {
            val reviewResponse = ReviewResponse(
                type = "GooglePlay",
                id = googlePlayReview.reviewId,
                userName = googlePlayReview.userName,
                userProfile = googlePlayReview.userProfilePicUrl,
                rating = googlePlayReview.rating,
//                title = googlePlayReview.,
                comment = googlePlayReview.comment,
                submitTime = googlePlayReview.submitTime,
                replyComment = googlePlayReview.replyComment,
                replySubmitTime = googlePlayReview.replySubmitTime,
                likeCount = googlePlayReview.likeCount,
                appVersion = googlePlayReview.appVersion,
                url = googlePlayReview.reviewUrl
            )
            reviews.add(reviewResponse)
        }

        return ReviewsResponse(
            next = reviewResult.token,
            reviews = reviews
        )
    }

    fun toResponse(reviewResult : AppStoreReviewsResult) : ReviewsResponse {
        var reviews = ArrayList<ReviewResponse>()
        val appStoreReviews = reviewResult.appStoreReviews
        for (appStoreReview in appStoreReviews) {

            val reviewResponse = ReviewResponse(
                    type = "AppStore",
                    id = appStoreReview.id,
                    userName = appStoreReview.attributes.userName,
                    rating = appStoreReview.attributes.rating,
                    title = appStoreReview.attributes.title,
                    comment = appStoreReview.attributes.review,
                    submitTime = Instant.parse(appStoreReview.attributes.date).toEpochMilli()
//                    replyComment = appStoreReview.attributes.developerResponse?.body,
//                    replySubmitTime = Instant.parse(appStoreReview.attributes.developerResponse?.modified?:"").toEpochMilli()
//                    url = appStoreReview.reviewUrl
            )
            reviews.add(reviewResponse)
        }

        return ReviewsResponse(
                next = reviewResult.next,
                reviews = reviews
        )
    }
}
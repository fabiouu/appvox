package com.appvox.appvox.converter

import com.appvox.appvox.domain.response.ReviewResponse
import com.appvox.appvox.domain.response.ReviewsResponse
import com.appvox.appvox.domain.result.googleplay.GooglePlayReviewsResult
import org.springframework.stereotype.Component


@Component
class GooglePlayReviewConverter {

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
}
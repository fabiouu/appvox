package com.appvox.core.review.converter

import com.appvox.core.review.domain.response.ReviewResponse
import com.appvox.core.review.domain.result.GooglePlayReviewResult

class GooglePlayReviewConverter {
    companion object {
        fun toResponse(reviewResult: GooglePlayReviewResult, nextCursor: String?): ReviewResponse {
            var reviews = ArrayList<ReviewResponse.Review>()
            val googlePlayReviews = reviewResult.reviews
            for (googlePlayReview in googlePlayReviews) {
                val reviewResponse = ReviewResponse.Review(
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

            return ReviewResponse(
                nextCursor = nextCursor,
                reviews = reviews
            )
        }
    }
}
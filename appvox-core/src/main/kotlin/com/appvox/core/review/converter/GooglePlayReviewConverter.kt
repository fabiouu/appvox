package com.appvox.core.review.converter

import com.appvox.core.review.domain.response.ReviewDTO
import com.appvox.core.review.domain.response.ReviewsResponse
import com.appvox.core.review.domain.result.GooglePlayReviewsResult

class GooglePlayReviewConverter {
    companion object {
        fun toResponse(reviewResult: GooglePlayReviewsResult, nextCursor: String?): ReviewsResponse {
            var reviews = ArrayList<ReviewDTO>()
            val googlePlayReviews = reviewResult.reviews
            for (googlePlayReview in googlePlayReviews) {
                val reviewResponse = ReviewDTO(
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
                nextCursor = nextCursor,
                reviews = reviews
            )
        }
    }
}
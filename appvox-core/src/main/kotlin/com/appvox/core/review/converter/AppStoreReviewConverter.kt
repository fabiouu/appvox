package com.appvox.core.review.converter

import com.appvox.core.review.domain.response.ReviewDTO
import com.appvox.core.review.domain.response.ReviewsResponse
import com.appvox.core.review.domain.result.AppStoreReviewsResult
import java.time.Instant

class AppStoreReviewConverter {
    companion object {
        fun toResponse(reviewResult: AppStoreReviewsResult, nextCursor: String?): ReviewsResponse {
            var reviews = ArrayList<ReviewDTO>()
            val appStoreReviews = reviewResult.data
            for (appStoreReview in appStoreReviews) {

                val reviewResponse = ReviewDTO(
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
                nextCursor = nextCursor,
                reviews = reviews
            )
        }
    }
}
package com.appvox.core.review.converter

import com.appvox.core.review.domain.response.ReviewResponse
import com.appvox.core.review.domain.result.AppStoreReviewResult
import java.time.Instant

class AppStoreReviewConverter {
    companion object {
        fun toResponse(reviewResult: AppStoreReviewResult, nextCursor: String?): ReviewResponse {
            var reviews = ArrayList<ReviewResponse.Review>()
            val appStoreReviews = reviewResult.data
            for (appStoreReview in appStoreReviews) {

                val reviewResponse = ReviewResponse.Review(
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

            return ReviewResponse(
                nextCursor = nextCursor,
                reviews = reviews
            )
        }
    }
}
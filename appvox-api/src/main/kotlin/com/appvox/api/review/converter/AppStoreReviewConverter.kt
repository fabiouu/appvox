package com.appvox.api.review.converter

import com.appvox.core.review.domain.response.ReviewResponse
import com.appvox.core.review.domain.response.ReviewsResponse
import com.appvox.core.review.domain.result.AppStoreReviewsResult
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class AppStoreReviewConverter {

    fun toResponse(reviewResult: AppStoreReviewsResult, nextCursor: String?) : ReviewsResponse {
        var reviews = ArrayList<ReviewResponse>()
        val appStoreReviews = reviewResult.data
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
                nextCursor = nextCursor,
                reviews = reviews
        )
    }
}
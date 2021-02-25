package dev.fabiou.appvox.core.review.converter

import dev.fabiou.appvox.core.review.domain.response.GooglePlayReviewResponse
import dev.fabiou.appvox.core.review.domain.result.GooglePlayReviewResult

class GooglePlayReviewConverter {
    companion object {
        fun toResponse(reviewResult: GooglePlayReviewResult): GooglePlayReviewResponse {
            var reviews = ArrayList<GooglePlayReviewResponse.GooglePlayReview>()
            val googlePlayReviews = reviewResult.reviews
            for (googlePlayReview in googlePlayReviews) {
                val reviewResponse = GooglePlayReviewResponse.GooglePlayReview(
                        id = googlePlayReview.reviewId,
                        userName = googlePlayReview.userName,
                        userAvatar = googlePlayReview.userProfilePicUrl,
                        rating = googlePlayReview.rating,
//                      title = googlePlayReview.,
                        comment = googlePlayReview.comment,
                        commentTime = googlePlayReview.submitTime,
                        replyComment = googlePlayReview.replyComment,
                        replyTime = googlePlayReview.replySubmitTime,
                        likeCount = googlePlayReview.likeCount,
                        appVersion = googlePlayReview.appVersion,
                        url = googlePlayReview.reviewUrl
                )
                reviews.add(reviewResponse)
            }

            return GooglePlayReviewResponse(
                reviews = reviews,
                nextToken = reviewResult.token
            )
        }
    }
}
package dev.fabiou.appvox.core.appstore.review.converter

import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewResponse
import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewResult
import dev.fabiou.appvox.core.archive.AppReviewResponse
import java.time.Instant
import java.time.ZonedDateTime

class AppStoreReviewConverter {
    companion object {
        fun toResponse(reviewResult: AppStoreReviewResult.AppStoreReview): AppStoreReviewResponse {
//            val appStoreReviews = reviewResult.data
//            for (appStoreReview in appStoreReviews) {
                val reviewResponse = AppStoreReviewResponse(
                    id = reviewResult.id,
                    userName = reviewResult.attributes.userName,
                    rating = reviewResult.attributes.rating,
                    title = reviewResult.attributes.title,
                    comment = reviewResult.attributes.review,
                    commentTime = ZonedDateTime.parse(reviewResult.attributes.date),
                    replyComment = reviewResult.attributes.developerResponse?.body,
                    replyTime = ZonedDateTime.parse(reviewResult.attributes.developerResponse?.modified),
    //                        url =
                )
//                reviews.add(reviewResponse)
//            }

            return reviewResponse
        }

//        internal fun toResponse(reviewResult: AppStoreRecentReviewResult): AppStoreReviewResponse {
//            var reviews = ArrayList<AppStoreReviewResponse.AppStoreReview>()
//            val appStoreReviews = reviewResult.entry
//            for (appStoreReview in appStoreReviews!!) {
//                val reviewResponse = AppStoreReviewResponse.AppStoreReview(
//                    id = appStoreReview.id!!,
//                    userName = appStoreReview.author?.name!!,
//                    rating = appStoreReview.rating!!,
//                    title = appStoreReview.title,
//                    comment = appStoreReview.content?.find { it.type == "text" }?.content!!,
////                    commentTime = appStoreReview.updated.
//                    version = appStoreReview.version,
//                    url = appStoreReview.link?.href//,
////                    likeCount = appStoreReview.voteCount,
////                    likeCount = appStoreReview.voteSum,
////                    replyComment = appStoreReview.attributes.developerResponse?.body,
////                    replySubmitTime = Instant.parse(appStoreReview.attributes.developerResponse?.modified?:"").toEpochMilli()
////                    url = appStoreReview.reviewUrl
//                )
//                reviews.add(reviewResponse)
//            }
//
//            return AppStoreReviewResponse(
//                reviews = reviews,
//                nextToken = reviewResult.link!!.find { it.rel == "next" }?.href!!
//            )
//        }
    }
}
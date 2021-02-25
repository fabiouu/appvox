package dev.fabiou.appvox.core.review.converter

import dev.fabiou.appvox.core.review.domain.response.AppStoreReviewResponse
import dev.fabiou.appvox.core.review.domain.result.AppStoreRecentReviewResult
import dev.fabiou.appvox.core.review.domain.result.AppStoreReviewResult
import java.time.Instant
import java.time.ZonedDateTime

class AppStoreReviewConverter {
    companion object {
        fun toResponse(reviewResult: AppStoreReviewResult): AppStoreReviewResponse {
            var reviews = ArrayList<AppStoreReviewResponse.AppStoreReview>()
            val appStoreReviews = reviewResult.data
            for (appStoreReview in appStoreReviews) {
                val reviewResponse = AppStoreReviewResponse.AppStoreReview(
                        id = appStoreReview.id,
                        userName = appStoreReview.attributes.userName,
                        rating = appStoreReview.attributes.rating,
                        title = appStoreReview.attributes.title,
                        comment = appStoreReview.attributes.review,
                        commentTime = ZonedDateTime.parse(appStoreReview.attributes.date),
                        replyComment = appStoreReview.attributes.developerResponse?.body,
                        replyTime = ZonedDateTime.parse(appStoreReview.attributes.developerResponse?.modified),
//                        url =
                )
                reviews.add(reviewResponse)
            }

            return AppStoreReviewResponse(
                    reviews = reviews,
                    nextToken = reviewResult.next
            )
        }

        internal fun toResponse(reviewResult: AppStoreRecentReviewResult): AppStoreReviewResponse {
            var reviews = ArrayList<AppStoreReviewResponse.AppStoreReview>()
            val appStoreReviews = reviewResult.entry
            for (appStoreReview in appStoreReviews!!) {
                val reviewResponse = AppStoreReviewResponse.AppStoreReview(
                    id = appStoreReview.id!!,
                    userName = appStoreReview.author?.name!!,
                    rating = appStoreReview.rating!!,
                    title = appStoreReview.title,
                    comment = appStoreReview.content?.find { it.type == "text" }?.content!!,
                    commentTime = appStoreReview.updated?.toGregorianCalendar()?.toZonedDateTime(),
                    appVersion = appStoreReview.version,
                    url = appStoreReview.link?.href,
                    likeCount = appStoreReview.voteCount,
//                    replyComment =
//                    replySubmitTime =
                )
                reviews.add(reviewResponse)
            }

            return AppStoreReviewResponse(
                reviews = reviews,
                nextToken = reviewResult.link!!.find { it.rel == "next" }?.href!!
            )
        }
    }
}
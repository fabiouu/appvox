package dev.fabiou.appvox.core.review.appstore

import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewResponse
import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewResult
import dev.fabiou.appvox.core.archive.AppReviewResponse
import dev.fabiou.appvox.core.review.appstore.domain.AppStoreReviewResult
import java.time.Instant
import java.time.ZonedDateTime

internal class AppStoreReviewConverter {
    companion object {
        fun toResponse(reviewResult: AppStoreReviewResult.AppStoreReview): AppStoreReviewResponse {
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
            return reviewResponse
        }
    }
}
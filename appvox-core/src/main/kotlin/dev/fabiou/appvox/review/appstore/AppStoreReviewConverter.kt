package dev.fabiou.appvox.review.appstore

import dev.fabiou.appvox.review.appstore.domain.AppStoreReview
import dev.fabiou.appvox.review.appstore.domain.AppStoreReviewResult
import java.time.ZonedDateTime

internal class AppStoreReviewConverter {
    fun toResponse(result: AppStoreReviewResult.AppStoreReview): AppStoreReview {
        return AppStoreReview(
            id = result.id,
            userName = result.attributes.userName,
            rating = result.attributes.rating,
            title = result.attributes.title,
            comment = result.attributes.review,
            commentTime = ZonedDateTime.parse(result.attributes.date),
            replyComment = result.attributes.developerResponse?.body,
            replyTime = ZonedDateTime.parse(result.attributes.developerResponse?.modified),
        )
    }
}

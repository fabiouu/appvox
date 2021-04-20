package dev.fabiou.appvox.review.appstore

import dev.fabiou.appvox.review.ReviewConverter
import dev.fabiou.appvox.review.appstore.domain.AppStoreReview
import dev.fabiou.appvox.review.appstore.domain.AppStoreReviewResult
import java.time.ZonedDateTime

internal class AppStoreReviewConverter : ReviewConverter<AppStoreReviewResult.AppStoreReview, AppStoreReview> {
    override fun toResponse(results: List<AppStoreReviewResult.AppStoreReview>): List<AppStoreReview> {
        val reviews = ArrayList<AppStoreReview>()
        results.forEach { result ->
            val review = AppStoreReview(
                id = result.id,
                userName = result.attributes.userName,
                rating = result.attributes.rating,
                title = result.attributes.title,
                comment = result.attributes.review,
                commentTime = ZonedDateTime.parse(result.attributes.date),
                replyComment = result.attributes.developerResponse?.body,
                replyTime = ZonedDateTime.parse(result.attributes.developerResponse?.modified),
            )
            reviews.add(review)
        }
        return reviews
    }
}

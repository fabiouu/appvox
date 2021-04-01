package dev.fabiou.appvox.core.review.appstore

import dev.fabiou.appvox.core.review.ReviewConverter
import dev.fabiou.appvox.core.review.appstore.domain.AppStoreReview
import dev.fabiou.appvox.core.review.appstore.domain.AppStoreReviewResult
import dev.fabiou.appvox.core.review.googleplay.domain.GooglePlayReview
import java.time.ZonedDateTime

internal class AppStoreReviewConverter : ReviewConverter<AppStoreReviewResult.AppStoreReview, AppStoreReview> {
    override fun toResponse(results: List<AppStoreReviewResult.AppStoreReview>): List<AppStoreReview> {
        val response = ArrayList<AppStoreReview>()
        for (result in results) {
            val review = AppStoreReview(
                id = result.id,
                userName = result.attributes.userName,
                rating = result.attributes.rating,
                title = result.attributes.title,
                comment = result.attributes.review,
                commentTime = ZonedDateTime.parse(result.attributes.date),
                replyComment = result.attributes.developerResponse?.body,
                replyTime = ZonedDateTime.parse(result.attributes.developerResponse?.modified),
                // url =
            )
            response.add(review)
        }
        return response
    }
}
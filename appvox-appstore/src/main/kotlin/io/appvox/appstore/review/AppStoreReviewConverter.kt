package io.appvox.appstore.review

import io.appvox.appstore.review.domain.AppStoreReview
import io.appvox.appstore.review.domain.AppStoreReviewRequestParameters
import io.appvox.appstore.review.domain.AppStoreReviewResult
import java.time.ZonedDateTime

internal class AppStoreReviewConverter {
    fun toResponse(
        requestParameters: AppStoreReviewRequestParameters,
        result: AppStoreReviewResult.AppStoreReview
    ): AppStoreReview {
        return AppStoreReview(
            id = result.id,
            region = requestParameters.region,
            userName = result.attributes.userName,
            rating = result.attributes.rating,
            title = result.attributes.title,
            comment = result.attributes.review,
            commentTime = ZonedDateTime.parse(result.attributes.date),
            replyComment = result.attributes.developerResponse?.body,
            replyTime = result.attributes.developerResponse?.let {
                ZonedDateTime.parse(result.attributes.developerResponse.modified)
            }
        )
    }
}

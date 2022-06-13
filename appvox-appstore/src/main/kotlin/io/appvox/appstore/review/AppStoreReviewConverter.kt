package io.appvox.appstore.review

import io.appvox.appstore.review.domain.AppStoreReview
import io.appvox.appstore.review.domain.AppStoreReviewRequestParameters
import io.appvox.appstore.review.domain.AppStoreReviewResult

internal class AppStoreReviewConverter {
    fun toResponse(
        requestParameters: AppStoreReviewRequestParameters,
        result: AppStoreReviewResult.Entry
    ): AppStoreReview {
        return AppStoreReview(
            id = result.id!!,
            region = requestParameters.region,
            url = result.link?.href,
            comments = arrayListOf(
                AppStoreReview.Comment(
                    userComment = AppStoreReview.UserComment(
                        username = result.author?.name!!,
                        rating = result.rating!!,
                        title = result.title,
                        text = result.content?.find { it.type == "text" }?.text!!,
                        time = result.updated?.toGregorianCalendar()?.toZonedDateTime(),
                        appVersion = result.version,
                        likeCount = result.voteCount ?: 0,
                    )
                )
            )
        )
    }
}

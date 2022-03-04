package io.appvox.appstore.review

import io.appvox.appstore.review.domain.ItunesRssReview
import io.appvox.appstore.review.domain.ItunesRssReviewRequestParameters
import io.appvox.appstore.review.domain.ItunesRssReviewResult

internal class ItunesRssReviewConverter {
    fun toResponse(
        requestParameters: ItunesRssReviewRequestParameters,
        result: ItunesRssReviewResult.Entry
    ): ItunesRssReview {
        return ItunesRssReview(
            id = result.id!!,
            region = requestParameters.region,
            url = result.link?.href,
            comments = arrayListOf(
                ItunesRssReview.Comment(
                    userComment = ItunesRssReview.UserComment(
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

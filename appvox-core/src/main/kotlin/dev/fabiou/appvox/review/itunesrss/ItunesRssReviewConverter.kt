package dev.fabiou.appvox.review.itunesrss

import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReview
import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReviewResult

internal class ItunesRssReviewConverter {
    fun toResponse(result: ItunesRssReviewResult.Entry): ItunesRssReview {
        return ItunesRssReview(
            id = result.id!!,
            url = result.link?.href,
            comments = arrayListOf(
                ItunesRssReview.Comment(
                    userComment = ItunesRssReview.UserComment(
                        userName = result.author?.name!!,
                        rating = result.rating!!,
                        title = result.title,
                        text = result.content?.find { it.type == "text" }?.text!!,
                        time = result.updated?.toGregorianCalendar()?.toZonedDateTime(),
                        appVersion = result.version,
                        likeCount = result.voteCount,
                    )
                )
            )
        )
    }
}

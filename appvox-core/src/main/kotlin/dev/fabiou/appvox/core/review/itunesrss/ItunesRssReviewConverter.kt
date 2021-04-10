package dev.fabiou.appvox.core.review.itunesrss

import dev.fabiou.appvox.core.review.ReviewConverter
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReview
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewResult

internal class ItunesRssReviewConverter : ReviewConverter<ItunesRssReviewResult.Entry, ItunesRssReview> {
        override fun toResponse(results: List<ItunesRssReviewResult.Entry>): List<ItunesRssReview> {
            val reviews = ArrayList<ItunesRssReview>()
            results.forEach { reviewResult ->
                val reviewResponse = ItunesRssReview(
                    id = reviewResult.id!!,
                    userName = reviewResult.author?.name!!,
                    rating = reviewResult.rating!!,
                    title = reviewResult.title,
                    comment = reviewResult.content?.find { it.type == "text" }?.content!!,
                    commentTime = reviewResult.updated?.toGregorianCalendar()?.toZonedDateTime(),
                    appVersion = reviewResult.version,
                    url = reviewResult.link?.href,
                    likeCount = reviewResult.voteCount,
        //                    replyComment =
        //                    replySubmitTime =
                )
                reviews.add(reviewResponse)
            }

            return reviews
        }
}

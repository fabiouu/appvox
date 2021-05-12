package dev.fabiou.appvox.review.itunesrss

import dev.fabiou.appvox.review.ReviewConverter
import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReview
import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReviewResult

internal class ItunesRssReviewConverter : ReviewConverter<ItunesRssReviewResult.Entry, ItunesRssReview> {
    override fun toResponse(results: List<ItunesRssReviewResult.Entry>): List<ItunesRssReview> {
        val reviews = ArrayList<ItunesRssReview>()
        results.forEach { result ->
            val review = ItunesRssReview(
                id = result.id!!,
                userName = result.author?.name!!,
                rating = result.rating!!,
                title = result.title,
                comment = result.content?.find { it.type == "text" }?.text!!,
                commentTime = result.updated?.toGregorianCalendar()?.toZonedDateTime(),
                appVersion = result.version,
                likeCount = result.voteCount,
                url = result.link?.href
            )
            reviews.add(review)
        }

        return reviews
    }
}

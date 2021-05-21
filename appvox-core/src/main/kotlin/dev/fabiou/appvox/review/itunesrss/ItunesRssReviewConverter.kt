package dev.fabiou.appvox.review.itunesrss

import dev.fabiou.appvox.review.ReviewConverter
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview
import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReview
import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReviewResult
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

internal class ItunesRssReviewConverter : ReviewConverter<ItunesRssReviewResult.Entry, ItunesRssReview> {
    override fun toResponse(results: List<ItunesRssReviewResult.Entry>): List<ItunesRssReview> {
        val reviews = ArrayList<ItunesRssReview>()
        results.forEach { result ->
            val review = ItunesRssReview(
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
            reviews.add(review)
        }

        return reviews
    }
}

package dev.fabiou.appvox.core.review.itunesrss

import dev.fabiou.appvox.core.review.ReviewConverter
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReview
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewResult

internal class ItunesRssReviewConverter : ReviewConverter<ItunesRssReviewResult.Entry, ItunesRssReview> {
//    companion object : ReviewConverter<ItunesRssReviewResult.Entry, ItunesRssReview> {
//        fun toResponse(reviewResult: ItunesRssReviewResult.Entry): ItunesRssReview {
//            val reviewResponse = ItunesRssReview(
//                id = reviewResult.id!!,
//                userName = reviewResult.author?.name!!,
//                rating = reviewResult.rating!!,
//                title = reviewResult.title,
//                comment = reviewResult.content?.find { it.type == "text" }?.content!!,
//                commentTime = reviewResult.updated?.toGregorianCalendar()?.toZonedDateTime(),
//                appVersion = reviewResult.version,
//                url = reviewResult.link?.href,
//                likeCount = reviewResult.voteCount,
////                    replyComment =
////                    replySubmitTime =
//            )
//            return reviewResponse
//        }

        override fun toResponse(reviewResults: List<ItunesRssReviewResult.Entry>): List<ItunesRssReview> {
            var reviews = ArrayList<ItunesRssReview>()
            for (reviewResult in reviewResults!!) {
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
//}
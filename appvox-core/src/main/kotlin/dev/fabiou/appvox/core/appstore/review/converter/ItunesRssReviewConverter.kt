package dev.fabiou.appvox.core.appstore.review.converter

import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewResponse
import dev.fabiou.appvox.core.appstore.review.domain.ItunesRssReviewResponse
import dev.fabiou.appvox.core.appstore.review.domain.ItunesRssReviewResult

internal class ItunesRssReviewConverter {
    companion object {
        fun toResponse(reviewResult: ItunesRssReviewResult.Entry): ItunesRssReviewResponse {
            val reviewResponse = ItunesRssReviewResponse(
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
            return reviewResponse
        }

        fun toResponse(reviewResults: List<ItunesRssReviewResult.Entry>): List<ItunesRssReviewResponse> {
            var reviews = ArrayList<ItunesRssReviewResponse>()
            for (reviewResult in reviewResults!!) {
                val reviewResponse = ItunesRssReviewResponse(
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
//            return reviewResponse
        }
    }
}
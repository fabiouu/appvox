package dev.fabiou.appvox.core.review.iterator

import dev.fabiou.appvox.core.exception.AppVoxErrorCode
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.review.domain.request.GooglePlayReviewRequest
import dev.fabiou.appvox.core.review.domain.response.GooglePlayReviewResponse
import dev.fabiou.appvox.core.review.facade.GooglePlayReviewFacade

class GooglePlayReviewIterator(
    val facade: GooglePlayReviewFacade,
    val appId: String,
    val request: GooglePlayReviewRequest
) : Iterable<GooglePlayReviewResponse.GooglePlayReview> {

    @Throws(AppVoxException::class)
    override fun iterator(): Iterator<GooglePlayReviewResponse.GooglePlayReview> {
        return object : Iterator<GooglePlayReviewResponse.GooglePlayReview> {

            var reviewIndex : Int = 0

            var iterator: Iterator<GooglePlayReviewResponse.GooglePlayReview>

            init {
                val response = facade.getReviewsByAppId(appId, request)
                iterator = response.reviews.iterator()
                request.nextToken = response.nextToken
            }

            override fun hasNext(): Boolean {

                if (facade.config.requestDelay < 500) {
                    throw AppVoxException(AppVoxErrorCode.REQ_DELAY_TOO_SHORT)
                }

                if (request.fetchCountLimit != 0 && reviewIndex == request.fetchCountLimit) {
                    return false
                }

                if (request.nextToken == null && !iterator.hasNext()) {
                    return false
                }

                if (!iterator.hasNext()) {
                    Thread.sleep(facade.config.requestDelay)
                    val response = facade.getReviewsByAppId(appId, request)
                    if (response.reviews.isEmpty()) {
                        return false
                    }
                    iterator = response.reviews.iterator()
                    request.nextToken = response.nextToken
                }

                return true
            }

            override fun next(): GooglePlayReviewResponse.GooglePlayReview {
                reviewIndex++
                return iterator.next()
            }
        }
    }
}

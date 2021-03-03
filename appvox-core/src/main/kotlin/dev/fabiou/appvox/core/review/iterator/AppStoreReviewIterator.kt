package dev.fabiou.appvox.core.review.iterator

import dev.fabiou.appvox.core.exception.AppVoxErrorCode
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.review.domain.request.AppStoreReviewRequest
import dev.fabiou.appvox.core.review.domain.response.ReviewResponse
import dev.fabiou.appvox.core.review.facade.AppStoreReviewFacade

class AppStoreReviewIterator(
    val facade: AppStoreReviewFacade,
    var request: AppStoreReviewRequest
) : Iterable<ReviewResponse.StoreReview> {

    @Throws(AppVoxException::class)
    override fun iterator(): Iterator<ReviewResponse.StoreReview> {
        return object : Iterator<ReviewResponse.StoreReview> {

            var reviewIndex : Int = 0

            var iterator: Iterator<ReviewResponse.StoreReview>

            init {
                val response = facade.getReviewsByAppId(request)
                iterator = response.reviews.iterator()
                request.nextToken = response.nextToken
            }

            override fun hasNext(): Boolean {

                if (facade.config.requestDelay < 500) {
                    throw AppVoxException(AppVoxErrorCode.REQ_DELAY_TOO_SHORT)
                }

                if (request.maxCount != 0 && reviewIndex == request.maxCount) {
                    return false
                }

                if (request.nextToken == null && !iterator.hasNext()) {
                    return false
                }

                if (!iterator.hasNext()) {
                    Thread.sleep(facade.config.requestDelay)
                    val response = facade.getReviewsByAppId(request)
                    if (response.reviews.isEmpty()) {
                        return false
                    }
                    iterator = response.reviews.iterator()
                    request.nextToken = response.nextToken
                }

                return true
            }

            override fun next(): ReviewResponse.StoreReview {
                reviewIndex++
                return iterator.next()
            }
        }
    }
}

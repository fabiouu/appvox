package dev.fabiou.appvox.core.review.iterator

import dev.fabiou.appvox.core.exception.AppVoxErrorCode
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.review.domain.request.AppReviewRequest
import dev.fabiou.appvox.core.review.domain.response.AppReviewResponse
import dev.fabiou.appvox.core.review.service.AppReviewService

class AppReviewIterator(
    val service: AppReviewService,
    val request: AppReviewRequest
) : Iterable<AppReviewResponse.AppReview> {

    @Throws(AppVoxException::class)
    override fun iterator(): Iterator<AppReviewResponse.AppReview> {
        return object : Iterator<AppReviewResponse.AppReview> {

            var reviewIndex : Int = 0

            var iterator: Iterator<AppReviewResponse.AppReview>

            init {
                val response = service.getReviewsByAppId(request)
                iterator = response.reviews.iterator()
                request.nextToken = response.nextToken
            }

            override fun hasNext(): Boolean {

                if (service.config.requestDelay < 500) {
                    throw AppVoxException(AppVoxErrorCode.REQ_DELAY_TOO_SHORT)
                }

                if (request.maxCount != 0 && reviewIndex == request.maxCount) {
                    return false
                }

                if (request.nextToken == null && !iterator.hasNext()) {
                    return false
                }

                if (!iterator.hasNext()) {
                    Thread.sleep(service.config.requestDelay)
                    val response = service.getReviewsByAppId(request)
                    if (response.reviews.isEmpty()) {
                        return false
                    }
                    iterator = response.reviews.iterator()
                    request.nextToken = response.nextToken
                }

                return true
            }

            override fun next(): AppReviewResponse.AppReview {
                reviewIndex++
                return iterator.next()
            }
        }
    }
}

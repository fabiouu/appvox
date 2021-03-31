package dev.fabiou.appvox.core.googleplay.review

import dev.fabiou.appvox.core.exception.AppVoxErrorCode
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.googleplay.review.domain.GooglePlayReviewRequest
import dev.fabiou.appvox.core.googleplay.review.domain.GooglePlayReviewResponse
import dev.fabiou.appvox.core.googleplay.review.domain.GooglePlayReviewResult

class GooglePlayReviewIterator(
        private val service: GooglePlayReviewService,
        private val request: GooglePlayReviewRequest
) : Iterator<GooglePlayReviewResponse.GooglePlayReview> {

    var currentIndex: Int = 0

    var iterator: Iterator<GooglePlayReviewResult.GooglePlayReview>

    init {
        val response = service.getReviewsByAppId(request)
        iterator = response.result.reviews.iterator()
        request.nextToken = response.nextToken
    }

    override fun hasNext(): Boolean {

        if (service.config.requestDelay < 500) {
            throw AppVoxException(AppVoxErrorCode.REQ_DELAY_TOO_SHORT)
        }

        if (request.maxCount != 0 && currentIndex == request.maxCount) {
            return false
        }

        if (request.nextToken == null && !iterator.hasNext()) {
            return false
        }

        if (!iterator.hasNext()) {
            Thread.sleep(service.config.requestDelay)
            val response = service.getReviewsByAppId(request)
            if (response.result.reviews.isEmpty()) {
                return false
            }
            iterator = response.result.reviews.iterator()
            request.nextToken = response.nextToken
        }

        return true
    }

    override fun next(): GooglePlayReviewResponse.GooglePlayReview {
        currentIndex++
        return GooglePlayReviewConverter.toResponse(iterator.next())
    }
}
package com.appvox.core.review.iterator

import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import com.appvox.core.review.domain.response.GooglePlayReviewResponse
import com.appvox.core.review.facade.GooglePlayReviewFacade

class GooglePlayReviewIterator(
    val facade: GooglePlayReviewFacade,
    val appId: String,
    val request: GooglePlayReviewRequest
) : Iterable<GooglePlayReviewResponse.GooglePlayReview> {

    override fun iterator(): Iterator<GooglePlayReviewResponse.GooglePlayReview> {
        return object : Iterator<GooglePlayReviewResponse.GooglePlayReview> {

            var reviewIndex : Int = 0

            var iterator: Iterator<GooglePlayReviewResponse.GooglePlayReview>

            init {
                val response = facade.getReviewsByAppId(appId, request)
                iterator = response.reviews.iterator()
                request.nextToken = response?.nextToken
            }

            override fun hasNext(): Boolean {

                if (request.fetchCountLimit != 0 && reviewIndex == request.fetchCountLimit) {
                    return false
                }

                if (request.nextToken == null && !iterator.hasNext()) {
                    return false
                }

                if (!iterator.hasNext()) {
                    Thread.sleep(facade.configuration.requestDelay)
                    val response = facade.getReviewsByAppId(appId, request)
                    if (response?.reviews == null || response.reviews.isEmpty()) {
                        return false
                    }
                    iterator = response.reviews.iterator()
                    request.nextToken = response?.nextToken
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

package com.appvox.core.review.iterator

import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import com.appvox.core.review.domain.response.GooglePlayReviewResponse
import com.appvox.core.review.facade.AppStoreReviewFacade
import com.appvox.core.review.facade.GooglePlayReviewFacade

class GooglePlayReviewIterator(
    val facade: GooglePlayReviewFacade,
    val appId: String,
    val request: GooglePlayReviewRequest
) : Iterable<GooglePlayReviewResponse.GooglePlayReview> {

    override fun iterator(): Iterator<GooglePlayReviewResponse.GooglePlayReview> {
        return object : Iterator<GooglePlayReviewResponse.GooglePlayReview> {

            var nextToken: String? = null
            var currentIndex = 0;
            var response: GooglePlayReviewResponse? = null

            override fun hasNext(): Boolean {
                return nextToken != null
            }

            override fun next(): GooglePlayReviewResponse.GooglePlayReview {
                if (currentIndex == response?.reviews?.size || response == null) {
                    Thread.sleep(3000)
                    response = facade.getReviewsByAppId(appId, GooglePlayReviewRequest("us", 1, 10, nextToken))
                    nextToken = response?.next
                    currentIndex = 0
                }
                val review = response?.reviews?.get(currentIndex)
                currentIndex++
                return review!!
            }
        }
    }
}

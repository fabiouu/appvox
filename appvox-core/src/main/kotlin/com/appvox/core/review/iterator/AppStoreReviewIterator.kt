package com.appvox.core.review.iterator

import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.response.AppStoreReviewResponse
import com.appvox.core.review.facade.AppStoreReviewFacade

class AppStoreReviewIterator(
    val facade: AppStoreReviewFacade,
    val appId: String,
    var request: AppStoreReviewRequest
) : Iterable<AppStoreReviewResponse.AppStoreReview> {

    override fun iterator(): Iterator<AppStoreReviewResponse.AppStoreReview> {
        return object : Iterator<AppStoreReviewResponse.AppStoreReview> {

            var reviewIndex : Int = 0

            var iterator: Iterator<AppStoreReviewResponse.AppStoreReview>

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

            override fun next(): AppStoreReviewResponse.AppStoreReview {
                reviewIndex++
                return iterator.next()
            }
        }
    }
}

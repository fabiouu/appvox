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

            var iterator: Iterator<AppStoreReviewResponse.AppStoreReview>
            var next : String?

            init {
                val response = facade.getReviewsByAppId(appId, request)
                iterator = response.reviews.iterator()
                next = response?.next
            }

            override fun hasNext(): Boolean {
                if (next == null && !iterator.hasNext()) {
                    return false
                }

                if (!iterator.hasNext()) {
                    Thread.sleep(3000)
                    val response = facade.getReviewsByAppId(appId, request)
                    if (response?.reviews == null || response.reviews.isEmpty()) {
                        return false
                    }
                    iterator = response.reviews.iterator()
                    next = response?.next
                }

                return true
            }

            override fun next(): AppStoreReviewResponse.AppStoreReview {
                return iterator.next()
//                if (currentIndex == response?.reviews?.size || response == null) {
//                    Thread.sleep(3000)
//                    response = facade.getReviewsByAppId(appId, request)
//                    next = response?.next!!
//                    currentIndex = 0
//                }
//                val review = response?.reviews?.iterator()
//                println("CURINDEX = $currentIndex")
//                currentIndex++
//                return review!!
            }
        }
    }
}

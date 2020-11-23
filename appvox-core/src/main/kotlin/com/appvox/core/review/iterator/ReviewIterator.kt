package com.appvox.core.review.iterator

import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.response.ReviewResponse
import com.appvox.core.review.facade.AppStoreReviewFacade

class AppReviewIterator(val facade: AppStoreReviewFacade, val appId: String) : Iterable<ReviewResponse.Review> {

//    val filter : String = ""

//    fun filter(init: Builder.() -> String) = apply { filter = init() }

    override fun iterator(): Iterator<ReviewResponse.Review> {
        return object : Iterator<ReviewResponse.Review> {

            var cursor = ""
            //            val state = from.sortedBy { it.size }
            var currentIndex = 0;
            var response : ReviewResponse? = null
            //
            override fun hasNext() : Boolean {
                if (cursor != null) {
                    return true
                } else {
                    return false
                }
            }

            override fun next() : ReviewResponse.Review {
                if (currentIndex + 1 == response?.reviews?.size || response == null ) {
                    Thread.sleep(3000)
                    response = facade.getReviewsByAppId(appId, AppStoreReviewRequest("us", 10, "eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkNSRjVITkJHUFEifQ.eyJpc3MiOiI4Q1UyNk1LTFM0IiwiaWF0IjoxNjA1MDQyMTIwLCJleHAiOjE2MDgwNjYxMjB9.bsgT71SiC_b9YEJp08xuoV1WtuVnukHGjNxAOBrN7IsVE-fEu7t_1gaOudctv5rWjebfG7e9to-JBjeE96jIEQ", "", cursor))
                    cursor = response?.nextCursor!!
                    currentIndex = 0
                }
                val review = response?.reviews?.get(currentIndex)
                currentIndex++
                return review!!
            }
        }
    }
}

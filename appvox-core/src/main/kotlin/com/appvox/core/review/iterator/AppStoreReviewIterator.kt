package com.appvox.core.review.iterator

import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.response.AppStoreReviewResponse
import com.appvox.core.review.domain.response.GooglePlayReviewResponse
import com.appvox.core.review.facade.AppStoreReviewFacade

class AppStoreReviewIterator(val facade: AppStoreReviewFacade, val appId: String) : Iterable<AppStoreReviewResponse.AppStoreReview> {

//    val filter : String = ""

//    fun filter(init: Builder.() -> String) = apply { filter = init() }

    override fun iterator(): Iterator<AppStoreReviewResponse.AppStoreReview> {
        return object : Iterator<AppStoreReviewResponse.AppStoreReview> {

            var cursor = ""
            var currentIndex = 0;
            var response : AppStoreReviewResponse? = null
            //
            override fun hasNext() : Boolean {
                if (cursor != null) {
                    return true
                } else {
                    return false
                }
            }

            override fun next() : AppStoreReviewResponse.AppStoreReview {
                if (currentIndex == response?.reviews?.size || response == null ) {
                    Thread.sleep(3000)
                    response = facade.getReviewsByAppId(appId, AppStoreReviewRequest("us", "eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkNSRjVITkJHUFEifQ.eyJpc3MiOiI4Q1UyNk1LTFM0IiwiaWF0IjoxNjA1MDQyMTIwLCJleHAiOjE2MDgwNjYxMjB9.bsgT71SiC_b9YEJp08xuoV1WtuVnukHGjNxAOBrN7IsVE-fEu7t_1gaOudctv5rWjebfG7e9to-JBjeE96jIEQ", "", cursor))
                    cursor = response?.next!!
                    currentIndex = 0
                }
                val review = response?.reviews?.get(currentIndex)
                println("CURINDEX = $currentIndex")
                currentIndex++
                return review!!
            }
        }
    }
}

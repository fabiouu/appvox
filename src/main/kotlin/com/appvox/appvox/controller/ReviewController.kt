package com.appvox.appvox.controller

import com.appvox.appvox.domain.request.review.AppStoreReviewRequest
import com.appvox.appvox.domain.request.review.GooglePlayReviewRequest
import com.appvox.appvox.domain.response.review.ReviewsResponse
import com.appvox.appvox.facade.AppStoreReviewFacade
import com.appvox.appvox.facade.GooglePlayReviewFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewController(
        @Autowired
        private val googlePlayReviewFacade: GooglePlayReviewFacade,

        @Autowired
        private val appStoreReviewFacade: AppStoreReviewFacade
) {

    @GetMapping("/store/google-play/app/{appId}/reviews")
    fun getReviewsByAppId(
            @PathVariable(value = "appId") appId: String,
            request : GooglePlayReviewRequest) : ReviewsResponse {
        val reviews = googlePlayReviewFacade.getReviewsByAppId(appId, request)
        return reviews
    }

    @GetMapping("/store/app-store/app/{appId}/reviews")
    fun getReviewsByAppId(
            @PathVariable(value = "appId") appId : String,
            request : AppStoreReviewRequest) : ReviewsResponse {
        val reviews = appStoreReviewFacade.getReviewsByAppId(appId, request)
        return reviews
    }
}
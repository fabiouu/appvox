package com.appvox.api.controller

import com.appvox.api.facade.AppStoreReviewFacade
import com.appvox.api.facade.GooglePlayReviewFacade
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import com.appvox.core.review.domain.response.ReviewsResponse
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
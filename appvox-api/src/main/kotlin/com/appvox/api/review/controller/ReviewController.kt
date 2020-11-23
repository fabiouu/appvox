package com.appvox.api.review.controller

import com.appvox.core.review.facade.AppStoreReviewFacade
import com.appvox.core.review.facade.GooglePlayReviewFacade
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import com.appvox.core.review.domain.response.ReviewResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewController {

    @GetMapping("/store/google-play/app/{appId}/reviews")
    fun getReviewsByAppId(
            @PathVariable(value = "appId") appId: String,
            request : GooglePlayReviewRequest) : ReviewResponse {
        val googlePlayReviewFacade = GooglePlayReviewFacade()
        val reviews = googlePlayReviewFacade.getReviewsByAppId(appId, request)
        return reviews
    }

    @GetMapping("/store/app-store/app/{appId}/reviews")
    fun getReviewsByAppId(
            @PathVariable(value = "appId") appId : String,
            request : AppStoreReviewRequest) : ReviewResponse {
        val appStoreReviewFacade = AppStoreReviewFacade()
        val reviews = appStoreReviewFacade.getReviewsByAppId(appId, request)
        return reviews
    }
}
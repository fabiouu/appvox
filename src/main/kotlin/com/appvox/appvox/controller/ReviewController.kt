package com.appvox.appvox.controller

import com.appvox.appvox.domain.response.ReviewsResponse
import com.appvox.appvox.facade.AppStoreReviewFacade
import com.appvox.appvox.facade.GooglePlayReviewFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
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
            @RequestParam(value = "language", defaultValue = "en", required = false) language: String,
            @RequestParam(value = "sortType", defaultValue = "1", required = false) sortType: Int,
            @RequestParam(value = "size", defaultValue = "40", required = false) size: Int,
            @RequestParam(value = "token", defaultValue = "", required = false) token: String) : ReviewsResponse {
        val reviews = googlePlayReviewFacade.getReviewsByAppId(appId, language, sortType, size, token)
        return reviews
    }

    @GetMapping("/store/app-store/app/{appId}/reviews")
    fun getReviewsByAppId(
            @PathVariable(value = "appId") appId: String,
            @RequestParam(value = "region", defaultValue = "us", required = false) region: String,
            @RequestParam(value = "size", defaultValue = "0", required = false) size: Int) : ReviewsResponse {
        val reviews = appStoreReviewFacade.getReviewsByAppId(appId, region, size)
        return reviews
    }

}
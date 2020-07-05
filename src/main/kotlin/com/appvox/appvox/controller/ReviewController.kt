package com.appvox.appvox.controller

import com.appvox.appvox.domain.response.ReviewsResponse
import com.appvox.appvox.facade.ReviewFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewController(
        @Autowired
        private val reviewFacade: ReviewFacade
) {

    @GetMapping("/app/{appId}/reviews")
    fun getReviewsByAppId(
            @PathVariable(value = "appId") appId: String,
            @RequestParam(value = "region", defaultValue = "us", required = false) region: String,
            @RequestParam(value = "language", defaultValue = "en", required = false) language: String,
            @RequestParam(value = "sortType", defaultValue = "1", required = false) sortType: Int,
            @RequestParam(value = "size", defaultValue = "40", required = false) size: Int,
            @RequestParam(value = "token", defaultValue = "", required = false) token: String) : ReviewsResponse {
        val reviews = reviewFacade.getReviewsByAppId(appId, region, language, sortType, size, token)
        return reviews
    }

}
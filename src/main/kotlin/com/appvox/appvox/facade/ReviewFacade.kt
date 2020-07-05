package com.appvox.appvox.facade

import com.appvox.appvox.converter.ReviewConverter
import com.appvox.appvox.domain.response.ReviewsResponse
import com.appvox.appvox.service.AppStoreReviewService
import com.appvox.appvox.service.GooglePlayReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.Double.parseDouble

@Service
class ReviewFacade(
        @Autowired
        private val appStoreReviewService : AppStoreReviewService,

        @Autowired
        private val googlePlayReviewService : GooglePlayReviewService,

        @Autowired
        private val reviewConverter: ReviewConverter
) {
    fun getReviewsByAppId(appId : String, region : String, language : String, sortType : Int, reviewCount: Int, token: String? = null) : ReviewsResponse {

        var reviewsResponse : ReviewsResponse
        if (isAppStoreApp(appId)) {
            val reviews = appStoreReviewService.getReviewsByAppId(appId, region, language, sortType, reviewCount, token)
            reviewsResponse = reviewConverter.toResponse(reviews)
        } else {
            val reviews = googlePlayReviewService.getReviewsByAppId(appId, region, language, sortType, reviewCount, token)
            reviewsResponse = reviewConverter.toResponse(reviews)
        }
        return reviewsResponse
    }

    private fun isAppStoreApp(appId : String) : Boolean {
        var numeric = true
        try {
            val num = parseDouble(appId)
        } catch (e: NumberFormatException) {
            numeric = false
        }
        return numeric
    }
}
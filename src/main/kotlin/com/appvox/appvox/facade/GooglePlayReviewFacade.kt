package com.appvox.appvox.facade

import com.appvox.appvox.converter.GooglePlayReviewConverter
import com.appvox.appvox.domain.response.ReviewsResponse
import com.appvox.appvox.service.AppStoreReviewService
import com.appvox.appvox.service.GooglePlayReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.Double.parseDouble

@Service
class GooglePlayReviewFacade(
        @Autowired
        private val googlePlayReviewService : GooglePlayReviewService,

        @Autowired
        private val googlePlayReviewConverter: GooglePlayReviewConverter
) {
    fun getReviewsByAppId(appId : String, language : String, sortType : Int, reviewCount: Int, token: String? = null) : ReviewsResponse {

        val reviews = googlePlayReviewService.getReviewsByAppId(appId, language, sortType, reviewCount, token)
        val reviewsResponse = googlePlayReviewConverter.toResponse(reviews)

        return reviewsResponse
    }
}
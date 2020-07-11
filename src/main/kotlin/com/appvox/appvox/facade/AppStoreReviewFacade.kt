package com.appvox.appvox.facade

import com.appvox.appvox.converter.AppStoreReviewConverter
import com.appvox.appvox.converter.GooglePlayReviewConverter
import com.appvox.appvox.domain.response.ReviewsResponse
import com.appvox.appvox.service.AppStoreReviewService
import com.appvox.appvox.service.GooglePlayReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.Double.parseDouble

@Service
class AppStoreReviewFacade(
        @Autowired
        private val appStoreReviewService : AppStoreReviewService,

        @Autowired
        private val appStoreReviewConverter : AppStoreReviewConverter
) {
    fun getReviewsByAppId(appId : String, region : String, reviewCount: Int) : ReviewsResponse {

        val reviews = appStoreReviewService.getReviewsByAppId(
                appId = appId,
                region = region,
                reviewCount = reviewCount
        )
        val reviewsResponse = appStoreReviewConverter.toResponse(reviews)

        return reviewsResponse
    }
}
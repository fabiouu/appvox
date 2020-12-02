package com.appvox.core.review.facade

import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.converter.AppStoreReviewConverter
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.response.AppStoreReviewResponse
import com.appvox.core.review.domain.response.GooglePlayReviewResponse
import com.appvox.core.review.service.AppStoreReviewService

class AppStoreReviewFacade(
        val configuration: ProxyConfiguration? = null
) {
    fun getReviewsByAppId(appId : String, request: AppStoreReviewRequest) : AppStoreReviewResponse {

        val service = AppStoreReviewService(configuration)

        var reviews = service.getReviewsByAppId(
            appId = appId,
            request = request
        )

        return AppStoreReviewConverter.toResponse(reviews!!)
    }
}
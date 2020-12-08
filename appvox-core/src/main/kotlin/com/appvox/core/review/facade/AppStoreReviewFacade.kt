package com.appvox.core.review.facade

import com.appvox.core.configuration.Configuration
import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.converter.AppStoreReviewConverter
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.response.AppStoreReviewResponse
import com.appvox.core.review.domain.response.GooglePlayReviewResponse
import com.appvox.core.review.service.AppStoreReviewService

class AppStoreReviewFacade(
        val configuration: Configuration
) {
    fun getReviewsByAppId(appId : String, request: AppStoreReviewRequest) : AppStoreReviewResponse {

        val service = AppStoreReviewService(configuration)

        if (null == request.bearerToken) {
            val bearerToken = service.getBearerToken(appId, request.region)
            request.bearerToken = bearerToken
        }

        var reviews = service.getReviewsByAppId(
            appId = appId,
            request = request
        )

        return AppStoreReviewConverter.toResponse(reviews!!)
    }
}
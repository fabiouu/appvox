package com.appvox.appvox.facade

import com.appvox.appvox.converter.AppStoreReviewConverter
import com.appvox.appvox.domain.request.review.AppStoreReviewRequest
import com.appvox.appvox.domain.response.review.ReviewsResponse
import com.appvox.appvox.domain.result.appstore.AppStoreReviewsResult
import com.appvox.appvox.helper.CursorHelper
import com.appvox.appvox.service.AppStoreReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class AppStoreReviewFacade(
        @Autowired
        private val appStoreReviewService : AppStoreReviewService,

        @Autowired
        private val appStoreReviewConverter : AppStoreReviewConverter
) {
    fun getReviewsByAppId(appId : String, request: AppStoreReviewRequest) : ReviewsResponse {

        var reviews : AppStoreReviewsResult
        var nextCursor = ""
        if (null != request.cursor) {
            val queryParameters = CursorHelper.decodeCursorToParameters(request.cursor!!)
            reviews = appStoreReviewService.getReviewsByAppId(
                    appId = appId,
                    request = AppStoreReviewRequest(
                        size = (queryParameters["size"] ?: error("")).toInt(),
                        region = queryParameters["region"] ?: error(""),
                        next = queryParameters["next"] ?: error(""),
                        cursor = null
                    )
            )
        } else {
            reviews = appStoreReviewService.getReviewsByAppId(
                    appId = appId,
                    request = request
            )
        }

        if (reviews.next != null && reviews.next!!.isNotEmpty()) {
            val queryParameters = hashMapOf<String, String>(
                    "region" to request.region,
                    "size" to request.size.toString(),
                    "next" to reviews.next!!
            )
            nextCursor = CursorHelper.encodeParametersToCursor(queryParameters)
        }

        val reviewsResponse = appStoreReviewConverter.toResponse(reviews, nextCursor)
        return reviewsResponse
    }
}
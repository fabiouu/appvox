package com.appvox.core.review.facade

import com.appvox.core.review.converter.AppStoreReviewConverter
import com.appvox.core.review.helper.CursorHelper
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.response.ReviewsResponse
import com.appvox.core.review.service.AppStoreReviewService

class AppStoreReviewFacade {
    fun getReviewsByAppId(appId : String, request: AppStoreReviewRequest) : ReviewsResponse {

        var facadeRequest = request
        if (null != request.cursor) {
            val queryParameters = CursorHelper.decodeCursorToParameters(request.cursor!!)
            facadeRequest = AppStoreReviewRequest(
                    size = (queryParameters["size"] ?: error("")).toInt(),
                    region = queryParameters["region"] ?: error(""),
                    next = queryParameters["next"] ?: error("")
            )
        }

        val service = AppStoreReviewService();
        var reviews = service.getReviewsByAppId(
            appId = appId,
            request = facadeRequest
        )

        var nextCursor : String? = null
        if (reviews?.next != null && reviews?.next!!.isNotEmpty()) {
            nextCursor = buildAppStoreCursor(facadeRequest.region, facadeRequest.size, reviews.next!!)
        }

        return AppStoreReviewConverter.toResponse(reviews!!, nextCursor)
    }

    fun buildAppStoreCursor(region : String, size : Int, next : String) : String {
        val queryParameters = hashMapOf(
            "region" to region,
            "size" to size.toString(),
            "next" to next
        )
        return CursorHelper.encodeParametersToCursor(queryParameters)
    }
}
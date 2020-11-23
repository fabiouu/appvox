package com.appvox.core.review.facade

import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.converter.AppStoreReviewConverter
import com.appvox.core.review.helper.CursorHelper
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.response.ReviewResponse
import com.appvox.core.review.service.AppStoreReviewService

class AppStoreReviewFacade(
        val configuration: ProxyConfiguration = null
) {
    fun getReviewsByAppId(appId : String, request: AppStoreReviewRequest) : ReviewResponse {

        val service = AppStoreReviewService(configuration)

        var facadeRequest = request
        if (null != request.cursor && !request.cursor.isNullOrEmpty()) {
            val queryParameters = CursorHelper.decodeCursorToParameters(request.cursor!!)
            facadeRequest = AppStoreReviewRequest(
                    size = (queryParameters["size"] ?: error("")).toInt(),
                    region = queryParameters["region"] ?: error(""),
                    bearerToken = service.getBearerToken(appId, queryParameters["region"] ?: "us"),
                    next = queryParameters["next"] ?: error("")
            )
        }

        var reviews = service.getReviewsByAppId(
            appId = appId,
            request = facadeRequest
        )

        var nextCursor : String? = null
        if (reviews?.next != null && reviews?.next!!.isNotEmpty()) {
            nextCursor = convertParametersToCursor(facadeRequest.region, facadeRequest.size, reviews.next!!)
        }

        return AppStoreReviewConverter.toResponse(reviews!!, nextCursor)
    }

//    fun convertCursorToParameters() :

    fun convertParametersToCursor(region : String, size : Int, next : String) : String {
        val queryParameters = hashMapOf(
            "region" to region,
            "size" to size.toString(),
            "next" to next
        )
        return CursorHelper.encodeParametersToCursor(queryParameters)
    }
}
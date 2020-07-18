package com.appvox.appvox.facade

import com.appvox.appvox.converter.AppStoreReviewConverter
import com.appvox.appvox.domain.request.review.AppStoreReviewRequest
import com.appvox.appvox.domain.response.review.ReviewsResponse
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

        var facadeRequest = request
        if (null != request.cursor) {
            val queryParameters = CursorHelper.decodeCursorToParameters(request.cursor!!)
            facadeRequest = AppStoreReviewRequest(
                size = (queryParameters["size"] ?: error("")).toInt(),
                region = queryParameters["region"] ?: error(""),
                next = queryParameters["next"] ?: error("")
            )
        }

        var reviews = appStoreReviewService.getReviewsByAppId(
            appId = appId,
            request = facadeRequest
        )

        var nextCursor : String? = null
        if (reviews.next != null && reviews.next!!.isNotEmpty()) {
            nextCursor = buildAppStoreCursor(facadeRequest.region, facadeRequest.size, reviews.next!!)
        }

        return appStoreReviewConverter.toResponse(reviews, nextCursor)
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
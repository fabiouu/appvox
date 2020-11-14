package com.appvox.core.review.facade

import com.appvox.core.configuration.Configuration
import com.appvox.core.review.converter.GooglePlayReviewConverter
import com.appvox.core.review.helper.CursorHelper
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import com.appvox.core.review.domain.response.ReviewsResponse
import com.appvox.core.review.service.GooglePlayReviewService

class GooglePlayReviewFacade(
    val configuration : Configuration? = null
) {
    fun getReviewsByAppId(appId : String, request: GooglePlayReviewRequest) : ReviewsResponse {

        var facadeRequest = request
        if (null != request.cursor && request.cursor!!.isNotEmpty()) {
            val queryParameters = CursorHelper.decodeCursorToParameters(request.cursor!!)
            facadeRequest = GooglePlayReviewRequest(
                    size = (queryParameters["size"] ?: error("")).toInt(),
                    language = queryParameters["language"] ?: error(""),
                    sortType = (queryParameters["sort"] ?: error("")).toInt(),
                    token = queryParameters["token"] ?: error("")
            )
        }

        val service = GooglePlayReviewService(configuration)
        val reviews = service.getReviewsByAppId(
                appId = appId,
                request = facadeRequest
        )

        var nextCursor : String? = null
        if (reviews.token != null && reviews.token!!.isNotEmpty()) {
            nextCursor = buildGooglePlayCursor(request.language, request.size, request.sortType, reviews.token!!)
        }

        return GooglePlayReviewConverter.toResponse(reviews, nextCursor)
    }

    fun buildGooglePlayCursor(language : String, size : Int, sort : Int, token : String) : String {
        val queryParameters = hashMapOf(
            "language" to language,
            "size" to size.toString(),
            "sort" to sort.toString(),
            "token" to token
        )
        return CursorHelper.encodeParametersToCursor(queryParameters)
    }
}
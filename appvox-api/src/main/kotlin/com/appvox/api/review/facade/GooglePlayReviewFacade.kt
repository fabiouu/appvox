package com.appvox.api.review.facade

import com.appvox.api.review.converter.GooglePlayReviewConverter
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import com.appvox.core.review.domain.response.ReviewsResponse
import com.appvox.api.helper.CursorHelper
import com.appvox.core.review.service.GooglePlayReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GooglePlayReviewFacade(
        @Autowired
        private val googlePlayReviewConverter: GooglePlayReviewConverter
) {
    fun getReviewsByAppId(appId : String, request: GooglePlayReviewRequest) : ReviewsResponse {

        var facadeRequest = request
        if (null != request.cursor && request.cursor!!.isNotEmpty()) {
            val queryParameters = CursorHelper.decodeCursorToParameters(request.cursor!!)
            facadeRequest = GooglePlayReviewRequest(
                    size = (queryParameters["size"] ?: error("")).toInt(),
                    language = queryParameters["language"] ?: error(""),
                    sort = (queryParameters["sort"] ?: error("")).toInt(),
                    token = queryParameters["token"] ?: error("")
            )
        }

        val reviews = GooglePlayReviewService.getReviewsByAppId(
                appId = appId,
                request = facadeRequest
        )

        var nextCursor : String? = null
        if (reviews.token != null && reviews.token!!.isNotEmpty()) {
            nextCursor = buildGooglePlayCursor(request.language, request.size, request.sort, reviews.token!!)
        }

        return googlePlayReviewConverter.toResponse(reviews, nextCursor)
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
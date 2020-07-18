package com.appvox.appvox.facade

import com.appvox.appvox.converter.GooglePlayReviewConverter
import com.appvox.appvox.domain.request.review.GooglePlayReviewRequest
import com.appvox.appvox.domain.response.review.ReviewsResponse
import com.appvox.appvox.helper.CursorHelper
import com.appvox.appvox.service.GooglePlayReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GooglePlayReviewFacade(
        @Autowired
        private val googlePlayReviewService : GooglePlayReviewService,

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

        val reviews = googlePlayReviewService.getReviewsByAppId(
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
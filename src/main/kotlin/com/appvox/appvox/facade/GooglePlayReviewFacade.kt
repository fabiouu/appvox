package com.appvox.appvox.facade

import com.appvox.appvox.converter.GooglePlayReviewConverter
import com.appvox.appvox.domain.request.review.GooglePlayReviewRequest
import com.appvox.appvox.domain.response.review.ReviewsResponse
import com.appvox.appvox.domain.result.googleplay.GooglePlayReviewsResult
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

        var reviews : GooglePlayReviewsResult
        var nextCursor = ""
        if (null != request.cursor) {
            val queryParameters = CursorHelper.decodeCursorToParameters(request.cursor!!)
            reviews = googlePlayReviewService.getReviewsByAppId(
                    appId = appId,
                    request = GooglePlayReviewRequest(
                        size = (queryParameters["size"] ?: error("")).toInt(),
                        language = queryParameters["language"] ?: error(""),
                        sort = (queryParameters["sort"] ?: error("")).toInt(),
                        token = queryParameters["token"] ?: error(""),
                        cursor = null
                    )
            )
        } else {
            reviews = googlePlayReviewService.getReviewsByAppId(
                    appId = appId,
                    request = request
            )

            if (reviews.token != null && reviews.token!!.isNotEmpty()) {
                val queryParameters = hashMapOf<String, String>(
                    "language" to request.language,
                    "size" to request.size.toString(),
                    "sort" to request.sort.toString(),
                    "token" to reviews.token!!
                )
                nextCursor = CursorHelper.encodeParametersToCursor(queryParameters)
            }

        }

        val reviewsResponse = googlePlayReviewConverter.toResponse(reviews, nextCursor)
        return reviewsResponse
    }
}
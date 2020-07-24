package com.appvox.core

import com.appvox.core.domain.request.review.GooglePlayReviewRequest
import com.appvox.core.domain.result.googleplay.GooglePlayReviewResult
import com.appvox.core.domain.result.googleplay.GooglePlayReviewsResult
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.core.Headers.Companion.CONTENT_TYPE
import com.github.kittinunf.fuel.httpPost
import java.awt.PageAttributes

object GooglePlayReviewService {

    private const val requestUrl : String = "https://play.google.com/_/PlayStoreUi/data/batchexecute?rpcids=UsvDTd&f.sid=-2417434988450146470&bl=boq_playuiserver_20200303.10_p0&hl=%s&authuser&soc-app=121&soc-platform=1&soc-device=1&_reqid=1080551"
    private const val intialRequestBody : String = "f.req=[[[\"UsvDTd\",\"[null,null,[2,%d,[%d,null,null],null,[]],[\\\"%s\\\",7]]\",null,\"generic\"]]]"
    private const val requestBodyWithToken : String = "f.req=[[[\"UsvDTd\",\"[null,null,[2,null,[%d,null,\\\"%s\\\"],null,[]],[\\\"%s\\\",7]]\",null,\"generic\"]]]"
    private const val reviewUrl : String = "https://play.google.com/_/PlayStoreUi/data/batchexecute?rpcids=UsvDTd&f.sid=-2417434988450146470&bl=boq_playuiserver_20200303.10_p0&hl=%s&authuser&soc-app=121&soc-platform=1&soc-device=1&_reqid=1080551"

    fun getReviewsByAppId(appId : String, request : GooglePlayReviewRequest) : GooglePlayReviewsResult {

        var requestBody : String
        if (request.token != null && request.token.isNotEmpty()) {
            requestBody = requestBodyWithToken.format(request.size, request.token, appId)
        } else {
            requestBody = intialRequestBody.format(request.sort, request.size, appId)
        }

        val requestUrl = requestUrl.format(request.language)
        val (request, response, result) = requestUrl
                .httpPost()
                .body(requestBody)
                .header(CONTENT_TYPE,  "application/x-www-form-urlencoded")
                .responseString()

        val gplayReviews = extractReviewsFromResponse(result.get())

        var reviewResults = ArrayList<GooglePlayReviewResult>()
        for (gplayReview in gplayReviews[0]) {
            val user = gplayReview[1]
            val userMetadata = user[1]
            val reply = gplayReview[7]
            val review = GooglePlayReviewResult(
                    reviewId = gplayReview[0].asText(),
                    userName = user[0].asText(),
                    userProfilePicUrl = userMetadata[3][2].asText(),
                    rating = gplayReview[2].asInt(),
                    comment = gplayReview[4].asText(),
                    submitTime = gplayReview[5][0].asLong(),
                    likeCount = gplayReview[6].asInt(),
                    appVersion = if (!gplayReview[10].isNull) gplayReview[10].asText() else null,
                    reviewUrl = reviewUrl.format(appId, gplayReview[0].asText()),
                    replyComment = if (!reply.isNull) reply[1].asText() else null,
                    replySubmitTime = if (!reply.isNull) reply[2][0].asLong() else null
            )
            reviewResults.add(review)
        }

        val token = if (!gplayReviews.isEmpty && !gplayReviews[1].isEmpty) gplayReviews[1][1] else null

        return GooglePlayReviewsResult(token = token?.asText(), reviews = reviewResults)
    }

    private fun extractReviewsFromResponse(gplayRes: String): JsonNode {
        val cleanGplayRes = gplayRes.substring(5)
        val gplayResRootArray = ObjectMapper().readTree(cleanGplayRes)
        val gplayResSubArray : JsonNode = gplayResRootArray[0][2]
        val gplayResSubArrayAsJsonString = gplayResSubArray.textValue()
        val gplayReviews = ObjectMapper().readTree(gplayResSubArrayAsJsonString)
        return gplayReviews
    }
}
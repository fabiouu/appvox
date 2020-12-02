package com.appvox.core.review.service

import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import com.appvox.core.review.domain.result.GooglePlayReviewResult
import com.appvox.core.utils.JsonUtils.getJsonNodeByIndex
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Headers.Companion.CONTENT_TYPE
import com.github.kittinunf.fuel.httpPost
import java.net.InetSocketAddress
import java.net.Proxy

internal class GooglePlayReviewService(
        val configuration: ProxyConfiguration? = null
) {

    private val requestUrl = "https://play.google.com/_/PlayStoreUi/data/batchexecute?rpcids=UsvDTd&f.sid=-2417434988450146470&bl=boq_playuiserver_20200303.10_p0&hl=%s&authuser&soc-app=121&soc-platform=1&soc-device=1&_reqid=1080551"
    private val requestBodyWithParams = "f.req=[[[\"UsvDTd\",\"[null,null,[2,%d,[%d,null,null],null,[]],[\\\"%s\\\",7]]\",null,\"generic\"]]]"
    private val requestBodyWithParamsAndToken = "f.req=[[[\"UsvDTd\",\"[null,null,[2,null,[%d,null,\\\"%s\\\"],null,[]],[\\\"%s\\\",7]]\",null,\"generic\"]]]"
    private val reviewUrl = "https://play.google.com/store/apps/details?id=%s&hl=%s&reviewId=%s"
    private val URL_FORM_CONTENT_TYPE = "application/x-www-form-urlencoded"

    private val REVIEW_ID_INDEX = arrayOf(0)
    private val USER_NAME_INDEX = arrayOf(1, 0)
    private val USER_PROFILE_PIC_INDEX = arrayOf(1, 1, 3, 2)
    private val RATING_INDEX = arrayOf(2)
    private val COMMENT_INDEX = arrayOf(4)
    private val SUBMIT_TIME_INDEX = arrayOf(5, 0)
    private val LIKE_COUNT_INDEX = arrayOf(6)
    private val APP_VERSION_INDEX = arrayOf(10)
    private val REPLY_COMMENT_INDEX = arrayOf(7, 1)
    private val REPLY_SUBMIT_TIME_INDEX = arrayOf(7, 2, 0)

    fun getReviewsByAppId(appId: String, request: GooglePlayReviewRequest): GooglePlayReviewResult {
        val requestBody = if (request.nextToken.isNullOrEmpty()) {
            requestBodyWithParams.format(request.sortType, request.size, appId)
        } else {
            requestBodyWithParamsAndToken.format(request.size, request.nextToken, appId)
        }

        if (null != configuration) {
            val addr = InetSocketAddress(configuration.host!!, configuration.port!!)
            FuelManager.instance.proxy = Proxy(Proxy.Type.HTTP, addr)
        }

        val requestUrl = requestUrl.format(request.language)
        val (httpRequest, response, result) = requestUrl
                .httpPost()
                .body(requestBody)
                .header(CONTENT_TYPE, URL_FORM_CONTENT_TYPE)
                .responseString()

        var reviewResults = ArrayList<GooglePlayReviewResult.GooglePlayReview>()
        val gplayReviews = extractReviewsFromResponse(result.get())
        for (gplayReview in gplayReviews[0]) {
            val review = GooglePlayReviewResult.GooglePlayReview(
                    reviewId = getJsonNodeByIndex(gplayReview, REVIEW_ID_INDEX).asText(),
                    userName = getJsonNodeByIndex(gplayReview, USER_NAME_INDEX).asText(),
                    userProfilePicUrl = getJsonNodeByIndex(gplayReview, USER_PROFILE_PIC_INDEX).asText(),
                    rating = getJsonNodeByIndex(gplayReview, RATING_INDEX).asInt(),
                    comment = getJsonNodeByIndex(gplayReview, COMMENT_INDEX).asText(),
                    submitTime = getJsonNodeByIndex(gplayReview, SUBMIT_TIME_INDEX).asLong(),
                    likeCount = getJsonNodeByIndex(gplayReview, LIKE_COUNT_INDEX).asInt(),
                    appVersion = getJsonNodeByIndex(gplayReview, APP_VERSION_INDEX).asText(),
                    reviewUrl = reviewUrl.format(
                            appId, request.language, getJsonNodeByIndex(gplayReview, REVIEW_ID_INDEX).asText()),
                    replyComment = getJsonNodeByIndex(gplayReview, REPLY_COMMENT_INDEX).asText(),
                    replySubmitTime = getJsonNodeByIndex(gplayReview, REPLY_SUBMIT_TIME_INDEX).asLong()
            )
            reviewResults.add(review)
        }

        val token = if (!gplayReviews.isEmpty && !gplayReviews[1].isEmpty) gplayReviews[1][1] else null

        return GooglePlayReviewResult(token = token?.asText(), reviews = reviewResults)
    }

    private fun extractReviewsFromResponse(gplayResponse: String): JsonNode {
        val cleanGplayResponse = gplayResponse.substring(5)
        val gplayRootArray = ObjectMapper().readTree(cleanGplayResponse)
        val gplaySubArray: JsonNode = gplayRootArray[0][2]
        val gplaySubArrayAsJsonString = gplaySubArray.textValue()
        val gplayReviews = ObjectMapper().readTree(gplaySubArrayAsJsonString)
        return gplayReviews
    }
}
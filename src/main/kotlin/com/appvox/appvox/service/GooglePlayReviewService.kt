package com.appvox.appvox.service

import com.appvox.appvox.domain.result.googleplay.GooglePlayReviewResult
import com.appvox.appvox.domain.result.googleplay.GooglePlayReviewsResult
import com.appvox.appvox.helper.HttpHelper
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service


@Service
class GooglePlayReviewService(
    @Autowired
    private val httpHelper : HttpHelper,

    @Value("\${scraper.googlePlay.request.url:}")
    private val requestUrl : String,

    @Value("\${scraper.googlePlay.request.body:}")
    private val intialRequestBody : String,

    @Value("\${scraper.googlePlay.request.bodyWithToken:}")
    private val requestBodyWithToken : String,

    @Value("\${scraper.googlePlay.review.url:}")
    private val reviewUrl : String
) {
    fun getReviewsByAppId(
            appId : String,
            language : String,
            sortType : Int,
            reviewCount: Int,
            token: String? = null) : GooglePlayReviewsResult {

        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
        var requestBody : String
        if (token != null && token.isNotEmpty()) {
            requestBody = requestBodyWithToken.format(sortType, reviewCount, token, appId)
        } else {
            requestBody = intialRequestBody.format(sortType, reviewCount, appId)
        }
        val request: HttpEntity<String> = HttpEntity(requestBody, requestHeaders)
        val requestUrl = requestUrl.format(language)
        val gplayResponse= httpHelper
                .getRestTemplate().postForEntity(requestUrl, request, String::class.java)
        val gplayReviews = extractReviewsFromResponse(gplayResponse.body!!)

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

        return GooglePlayReviewsResult(token = token?.asText(), googlePlayReviews = reviewResults)
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
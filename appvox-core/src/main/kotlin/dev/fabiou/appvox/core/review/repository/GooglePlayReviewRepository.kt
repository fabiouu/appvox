package dev.fabiou.appvox.core.review.repository

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.exception.AppVoxErrorCode
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.review.domain.request.GooglePlayReviewRequest
import dev.fabiou.appvox.core.review.domain.result.GooglePlayReviewResult
import dev.fabiou.appvox.core.utils.HttpUtils
import dev.fabiou.appvox.core.utils.JsonUtils.getJsonNodeByIndex
import dev.fabiou.appvox.core.utils.impl.HttpUtilsImpl
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import dev.fabiou.appvox.core.review.domain.request.ReviewRequest
import dev.fabiou.appvox.core.utils.UrlUtils

internal class GooglePlayReviewRepository(
        private val config: Configuration? = null
) {
    companion object {
        internal const val REQUEST_URL_DOMAIN = "https://play.google.com"
        internal const val REQUEST_URL_PATH = "/_/PlayStoreUi/data/batchexecute"
        private const val REQUEST_URL_PARAMS = "?rpcids=UsvDTd&f.sid=-2417434988450146470&bl=boq_playuiserver_20200303.10_p0&hl=%s&authuser&soc-app=121&soc-platform=1&soc-device=1&_reqid=1080551"
        private const val REQUEST_BODY_WITH_PARAMS = "f.req=[[[\"UsvDTd\",\"[null,null,[2,%d,[%d,null,null],null,[]],[\\\"%s\\\",7]]\",null,\"generic\"]]]"
        private const val REQUEST_BODY_WITH_PARAMS_AND_BODY = "f.req=[[[\"UsvDTd\",\"[null,null,[2,null,[%d,null,\\\"%s\\\"],null,[]],[\\\"%s\\\",7]]\",null,\"generic\"]]]"
        private const val REVIEW_URL = "https://play.google.com/store/apps/details?id=%s&hl=%s&reviewId=%s"

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
    }

    private var httpUtils: HttpUtils = HttpUtilsImpl

    @Throws(AppVoxException::class)
    fun getReviewsByAppId(request: ReviewRequest, nextToken: String? = null): GooglePlayReviewResult {
        if (request.batchSize !in 1..100) {
            throw AppVoxException(AppVoxErrorCode.INVALID_ARGUMENT)
        }

        val requestBody = if (nextToken.isNullOrEmpty()) {
            REQUEST_BODY_WITH_PARAMS.format(request.sortType.sortType, request.batchSize, request.appId)
        } else {
            REQUEST_BODY_WITH_PARAMS_AND_BODY.format(request.batchSize, nextToken, request.appId)
        }

        val requestUrl = UrlUtils.getUrlDomainByEnv(REQUEST_URL_DOMAIN) +
                REQUEST_URL_PATH + REQUEST_URL_PARAMS.format(request.language.langCode)
        val responseContent = httpUtils.postRequest(requestUrl, requestBody, config?.proxy)

        val reviewResults = ArrayList<GooglePlayReviewResult.GooglePlayReview>()
        val gplayReviews = extractReviewsFromResponse(responseContent)
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
                    reviewUrl = REVIEW_URL.format(
                            request.appId, request.language.langCode, getJsonNodeByIndex(gplayReview, REVIEW_ID_INDEX).asText()),
                    replyComment = getJsonNodeByIndex(gplayReview, REPLY_COMMENT_INDEX).asText(),
                    replySubmitTime = getJsonNodeByIndex(gplayReview, REPLY_SUBMIT_TIME_INDEX).asLong()
            )
            reviewResults.add(review)
        }

        val token = if (!gplayReviews.isEmpty && !gplayReviews[1].isEmpty) gplayReviews[1][1] else null

        return GooglePlayReviewResult(token = token?.asText(), reviews = reviewResults)
    }

    private fun extractReviewsFromResponse(gPlayResponse: String): JsonNode {
        val cleanResponse = gPlayResponse.substring(4)
        val rootArray = ObjectMapper().readTree(cleanResponse)
        val subArray: JsonNode = rootArray[0][2]
        val subArrayAsJsonString = subArray.textValue()
        val reviews = ObjectMapper().readTree(subArrayAsJsonString)
        return reviews
    }
}
package dev.fabiou.appvox.review.googleplay

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.NullNode
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.exception.AppVoxError.DESERIALIZATION
import dev.fabiou.appvox.exception.AppVoxError.INVALID_ARGUMENT
import dev.fabiou.appvox.exception.AppVoxException
import dev.fabiou.appvox.exception.AppVoxNetworkException
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.ReviewResult
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewRequestParameters
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewResult
import dev.fabiou.appvox.util.HttpUtil
import dev.fabiou.appvox.util.JsonUtil.getJsonNodeByIndex

internal class GooglePlayReviewRepository(
    private val config: RequestConfiguration
) {

    companion object {
        internal var REQUEST_URL_DOMAIN = "https://play.google.com"
        internal const val REQUEST_URL_PATH = "/_/PlayStoreUi/data/batchexecute"
        private const val REQUEST_URL_PARAMS = "?rpcids=UsvDTd" +
            "&f.sid=%s" +
            "&bl=%s" +
            "&hl=%s" +
            "&authuser" +
            "&soc-app=121" +
            "&soc-platform=1" +
            "&soc-device=1" +
            "&_reqid=%s"
        private const val REQUEST_BODY_WITH_PARAMS =
            "f.req=[[[\"UsvDTd\",\"[null,null,[2,%d,[%d,null,null],null,[null,null,%s]],[\\\"%s\\\",7]]\",null,\"generic\"]]]"
        private const val REQUEST_BODY_WITH_PARAMS_AND_BODY =
            "f.req=[[[\"UsvDTd\",\"[null,null,[2,null,[%d,null,\\\"%s\\\"],null,[null,null,%s]],[\\\"%s\\\",7]]\",null,\"generic\"]]]"
        private const val REQUEST_BODY_HISTORY =
            "f.req=[[[\"UsvDTd\",\"[null,null,[4,null,[%d]],[\\\"%s\\\",7],\\\"%s\\\"]\",null,\"1\"]]]"
        private const val REVIEW_URL = "https://play.google.com/store/apps/details?id=%s&hl=%s&reviewId=%s"

        private val ROOT_ARRAY_INDEX = intArrayOf(0, 2)
        private val REVIEW_ID_INDEX = intArrayOf(0)
        private val USER_NAME_INDEX = intArrayOf(1, 0)
        private val USER_PROFILE_PIC_INDEX = intArrayOf(1, 1, 3, 2)
        private val RATING_INDEX = intArrayOf(2)
        private val COMMENT_INDEX = intArrayOf(4)
        private val SUBMIT_TIME_INDEX = intArrayOf(5, 0)
        private val LIKE_COUNT_INDEX = intArrayOf(6)
        private val APP_VERSION_INDEX = intArrayOf(10)
        private val REPLY_COMMENT_INDEX = intArrayOf(7, 1)
        private val REPLY_SUBMIT_TIME_INDEX = intArrayOf(7, 2, 0)
        private val CRITERIA_INDEX = intArrayOf(12)
        private val CRITERIA_CATEGORY_INDEX = intArrayOf(0)
        private val CRITERIA_RATING_INDEX = intArrayOf(1, 0)
        private val HAS_EDIT_HISTORY = intArrayOf(14, 0)
        private val TOKEN_INDEX = intArrayOf(1, 1)

        private const val GOOGLE_PLAY_SUB_RESPONSE_START_INDEX = 4

        private const val MIN_BATCH_SIZE = 1
        private const val MAX_BATCH_SIZE = 150

        private const val MIN_RANDOM_REQID_INIT_NUMBER = 0
        private const val MAX_RANDOM_REQID_INIT_NUMBER = 9999
        private const val REQID_INCREMENT = 100000
    }

    private val httpUtils = HttpUtil

    private var reqId = (MIN_RANDOM_REQID_INIT_NUMBER..MAX_RANDOM_REQID_INIT_NUMBER).random()

    fun getReviewsByAppId(
        request: ReviewRequest<GooglePlayReviewRequestParameters>
    ): ReviewResult<GooglePlayReviewResult> {
        if (request.parameters.batchSize !in MIN_BATCH_SIZE..MAX_BATCH_SIZE) {
            throw AppVoxException(INVALID_ARGUMENT)
        }

        val requestUrl = buildRequestUrl(request)
        val requestBody = if (request.nextToken.isNullOrEmpty()) {
            REQUEST_BODY_WITH_PARAMS.format(
                request.parameters.sortType.sortType,
                request.parameters.batchSize,
                request.parameters.deviceName?.let { "\\\"${request.parameters.deviceName}\\\"" },
                request.parameters.appId
            )
        } else {
            REQUEST_BODY_WITH_PARAMS_AND_BODY.format(
                request.parameters.batchSize,
                request.nextToken,
                request.parameters.deviceName?.let { "\\\"${request.parameters.deviceName}\\\"" },
                request.parameters.appId
            )
        }
        val responseContent = httpUtils.postRequest(requestUrl, requestBody, config.proxy)
        val reviews = ArrayList<GooglePlayReviewResult>()
        val googlePlayResponse = parseReviewsFromResponse(responseContent)
        if (googlePlayResponse.isEmpty) {
            throw AppVoxNetworkException(DESERIALIZATION)
        }
        val googlePlayRawReviews = googlePlayResponse.first()
        for (googlePlayRawReview in googlePlayRawReviews) {
            val review = parseReviewFromResponse(request, googlePlayRawReview)
            reviews.add(review)
        }

        val tokenJsonNode = getJsonNodeByIndex(googlePlayResponse, TOKEN_INDEX)
        return ReviewResult(
            results = reviews,
            nextToken = tokenJsonNode.asText(null)
        )
    }

    fun getReviewHistoryById(
        reviewId: String,
        request: ReviewRequest<GooglePlayReviewRequestParameters>
    ): List<GooglePlayReviewResult> {
        if (request.parameters.batchSize !in MIN_BATCH_SIZE..MAX_BATCH_SIZE) {
            throw AppVoxException(INVALID_ARGUMENT)
        }

        val requestUrl = buildRequestUrl(request)
        val requestBody = REQUEST_BODY_HISTORY.format(
            request.parameters.batchSize,
            request.parameters.appId,
            reviewId
        )
        val responseContent = httpUtils.postRequest(requestUrl, requestBody, config.proxy)
        val reviews = ArrayList<GooglePlayReviewResult>()
        val googlePlayResponse = parseReviewsFromResponse(responseContent)
        if (googlePlayResponse.isEmpty) {
            throw AppVoxNetworkException(DESERIALIZATION)
        }
        val googlePlayRawReviews = googlePlayResponse.first()
        for (googlePlayRawReview in googlePlayRawReviews) {
            val review = parseReviewFromResponse(request, googlePlayRawReview)
            reviews.add(review)
        }

        return reviews
    }

    private fun parseReviewFromResponse(
        request: ReviewRequest<GooglePlayReviewRequestParameters>,
        googlePlayRawReview: JsonNode
    ): GooglePlayReviewResult {
        return GooglePlayReviewResult(
            reviewId = getJsonNodeByIndex(googlePlayRawReview, REVIEW_ID_INDEX).asText(),
            userName = getJsonNodeByIndex(googlePlayRawReview, USER_NAME_INDEX).asText(),
            userProfilePicUrl = getJsonNodeByIndex(googlePlayRawReview, USER_PROFILE_PIC_INDEX).asText(),
            rating = getJsonNodeByIndex(googlePlayRawReview, RATING_INDEX).asInt(),
            userCommentText = getJsonNodeByIndex(googlePlayRawReview, COMMENT_INDEX).asText(),
            userCommentTime = getJsonNodeByIndex(googlePlayRawReview, SUBMIT_TIME_INDEX).asLong(),
            likeCount = getJsonNodeByIndex(googlePlayRawReview, LIKE_COUNT_INDEX).asInt(),
            appVersion = getJsonNodeByIndex(googlePlayRawReview, APP_VERSION_INDEX).asText(),
            criterias = emptyList(),
            reviewUrl = REVIEW_URL.format(
                request.parameters.appId,
                request.parameters.language.code,
                getJsonNodeByIndex(googlePlayRawReview, REVIEW_ID_INDEX).asText()
            ),
            hasEditHistory = getJsonNodeByIndex(googlePlayRawReview, HAS_EDIT_HISTORY).whenNotNull { it.asBoolean() },
            developerCommentText = getJsonNodeByIndex(googlePlayRawReview, REPLY_COMMENT_INDEX)
                .whenNotNull { it.asText() },
            developerCommentTime = getJsonNodeByIndex(googlePlayRawReview, REPLY_SUBMIT_TIME_INDEX)
                .whenNotNull { it.asLong() }
        )
    }

    private fun buildRequestUrl(
        request: ReviewRequest<GooglePlayReviewRequestParameters>
    ): String {
        reqId += REQID_INCREMENT
        return REQUEST_URL_DOMAIN +
            REQUEST_URL_PATH +
            REQUEST_URL_PARAMS.format(
                request.parameters.sid, request.parameters.bl, request.parameters.language.code, reqId
            )
    }

    private fun parseReviewsFromResponse(gPlayResponse: String): JsonNode {
        val cleanResponse = gPlayResponse.substring(GOOGLE_PLAY_SUB_RESPONSE_START_INDEX)
        val rootNode = ObjectMapper().readTree(cleanResponse)
        val subNode = getJsonNodeByIndex(rootNode, ROOT_ARRAY_INDEX)
        val subNodeAsString = subNode.textValue()
        return if (subNodeAsString != null) {
            ObjectMapper().readTree(subNodeAsString)
        } else {
            NullNode.getInstance()
        }
    }

    private inline fun <R> JsonNode.whenNotNull(block: (JsonNode) -> R): R? {
        return if (!this.isNull) block(this) else null
    }
}

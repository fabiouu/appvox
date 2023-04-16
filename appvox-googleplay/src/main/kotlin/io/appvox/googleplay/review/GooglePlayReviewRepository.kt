package io.appvox.googleplay.review

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.NullNode
import io.appvox.core.configuration.RequestConfiguration
import io.appvox.core.exception.AppVoxError.*
import io.appvox.core.exception.AppVoxException
import io.appvox.core.review.ReviewResult
import io.appvox.core.util.HttpUtil
import io.appvox.core.util.JsonUtil.getJsonNodeByIndex
import io.appvox.core.util.JsonUtil.whenNotNull
import io.appvox.googleplay.review.domain.GooglePlayReview
import io.appvox.googleplay.review.domain.GooglePlayReviewRequest
import java.io.IOException
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

internal class GooglePlayReviewRepository(private val config: RequestConfiguration) {

    companion object {
        internal var REQUEST_URL_DOMAIN = "https://play.google.com"
        internal const val REQUEST_URL_PATH = "/_/PlayStoreUi/data/batchexecute"
        private const val REQUEST_URL_PARAMS =
            "?rpcids=UsvDTd&f.sid=%s&bl=%s&hl=%s&authuser&soc-app=121&soc-platform=1&soc-device=1&_reqid=%s"
        private const val REQUEST_BODY_WITH_PARAMS =
            "f.req=[[[\"UsvDTd\",\"[null,null,[2,%d,[%d,null,null],null,[null,null,%s]],[\\\"%s\\\",7]]\",null,\"generic\"]]]"
        private const val REQUEST_BODY_WITH_PARAMS_AND_BODY =
            "f.req=[[[\"UsvDTd\",\"[null,null,[2,null,[%d,null,\\\"%s\\\"],null,[null,null,%s]],[\\\"%s\\\",7]]\",null,\"generic\"]]]"
        private const val REQUEST_BODY_HISTORY =
            "f.req=[[[\"UsvDTd\",\"[null,null,[4,null,[%d]],[\\\"%s\\\",7],\\\"%s\\\"]\",null,\"1\"]]]"

        private const val GOOGLE_PLAY_SUB_RESPONSE_START_INDEX = 4

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

        // TODO: Implement criteria fetching
        private val CRITERIA_INDEX = intArrayOf(12)
        private val CRITERIA_CATEGORY_INDEX = intArrayOf(0)
        private val CRITERIA_RATING_INDEX = intArrayOf(1, 0)
        private val HAS_EDIT_HISTORY_INDEX = intArrayOf(14, 0)
        private val NEXT_TOKEN_INDEX = intArrayOf(1, 1)

        private const val MIN_BATCH_SIZE = 1
        private const val MAX_BATCH_SIZE = 150

        private const val MIN_RANDOM_REQID_INIT_NUMBER = 0
        private const val MAX_RANDOM_REQID_INIT_NUMBER = 9999
        private const val REQID_INCREMENT = 100000
    }

    private val httpUtils = HttpUtil

    private var reqId = (MIN_RANDOM_REQID_INIT_NUMBER..MAX_RANDOM_REQID_INIT_NUMBER).random()

    fun getReviewsByAppId(request: GooglePlayReviewRequest): ReviewResult<GooglePlayReview> {
        if (request.batchSize !in MIN_BATCH_SIZE..MAX_BATCH_SIZE) {
            throw AppVoxException(INVALID_ARGUMENT)
        }

        val requestUrl = buildRequestUrl(request)
        val requestBody = if (request.nextToken.isNullOrEmpty()) {
            REQUEST_BODY_WITH_PARAMS.format(
                request.sortType.sortType,
                request.batchSize,
                request.deviceName?.let { "\\\"${request.deviceName}\\\"" },
                request.appId
            )
        } else {
            REQUEST_BODY_WITH_PARAMS_AND_BODY.format(
                request.batchSize,
                request.nextToken,
                request.deviceName?.let { "\\\"${request.deviceName}\\\"" },
                request.appId
            )
        }

        val responseContent = try {
            httpUtils.postRequest(requestUrl, requestBody, config.proxy)
        } catch (e: IOException) {
            throw AppVoxException(NETWORK, e)
        }
        val rawReviews = parseReviewsFromResponse(responseContent)
        if (rawReviews.isNull || rawReviews.isEmpty) {
            throw AppVoxException(DESERIALIZATION)
        }

        val reviews = mutableListOf<GooglePlayReview>()
        for (rawReview in rawReviews.first()) {
            val review = parseReviewFromResponse(request, rawReview)
            reviews.add(review)
        }

        val tokenJsonNode = getJsonNodeByIndex(rawReviews, NEXT_TOKEN_INDEX)
        return ReviewResult(
            results = reviews,
            nextToken = tokenJsonNode.asText(null)
        )
    }

    fun getReviewCommentHistoryById(
        reviewId: String,
        request: GooglePlayReviewRequest
    ): List<GooglePlayReview.UserComment> {
        if (request.batchSize !in MIN_BATCH_SIZE..MAX_BATCH_SIZE) {
            throw AppVoxException(INVALID_ARGUMENT)
        }
        val requestUrl = buildRequestUrl(request)
        val requestBody = REQUEST_BODY_HISTORY.format(
            request.batchSize,
            request.appId,
            reviewId
        )
        val responseContent = httpUtils.postRequest(requestUrl, requestBody, config.proxy)
        val userComments = mutableListOf<GooglePlayReview.UserComment>()
        val googlePlayResponse = parseReviewsFromResponse(responseContent)
        if (googlePlayResponse.isEmpty) {
            throw AppVoxException(DESERIALIZATION)
        }
        val googlePlayRawReviews = googlePlayResponse.first()
        for (googlePlayRawReview in googlePlayRawReviews) {
            val userComment = parseUserCommentFromResponse(googlePlayRawReview)
            userComments.add(userComment)
        }
        return userComments
    }

    private fun parseReviewFromResponse(
        request: GooglePlayReviewRequest,
        googlePlayRawReview: JsonNode
    ): GooglePlayReview {
        return GooglePlayReview(
            id = getJsonNodeByIndex(googlePlayRawReview, REVIEW_ID_INDEX).asText(),
            language = request.language,
            userComments = listOf(
                parseUserCommentFromResponse(googlePlayRawReview)
            ),
            developerComments = getJsonNodeByIndex(googlePlayRawReview, REPLY_COMMENT_INDEX)
                .whenNotNull { it.asText() }?.let { developerCommentText ->
                    listOf(
                        GooglePlayReview.DeveloperComment(
                            text = developerCommentText,
                            latestUpdateTime = ZonedDateTime.ofInstant(
                                Instant.ofEpochSecond(
                                    getJsonNodeByIndex(googlePlayRawReview, REPLY_SUBMIT_TIME_INDEX)
                                        .whenNotNull { it.asLong() }!!
                                ),
                                ZoneOffset.UTC
                            ),
                        )
                    )
                }.orEmpty(),
            hasEditHistory = getJsonNodeByIndex(
                googlePlayRawReview,
                HAS_EDIT_HISTORY_INDEX
            ).whenNotNull { it.asBoolean() } ?: false,
        )
    }

    private fun parseUserCommentFromResponse(response: JsonNode): GooglePlayReview.UserComment =
        GooglePlayReview.UserComment(
            username = getJsonNodeByIndex(response, USER_NAME_INDEX).asText(),
            avatar = getJsonNodeByIndex(response, USER_PROFILE_PIC_INDEX).asText(),
            rating = getJsonNodeByIndex(response, RATING_INDEX).asInt(),
            title = null, // TODO Title contained in text seprated by \t
            text = getJsonNodeByIndex(response, COMMENT_INDEX).asText(),
            latestUpdateTime = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(getJsonNodeByIndex(response, SUBMIT_TIME_INDEX).asLong()),
                ZoneOffset.UTC
            ),
            likeCount = getJsonNodeByIndex(response, LIKE_COUNT_INDEX).asInt(),
            appVersion = getJsonNodeByIndex(response, APP_VERSION_INDEX).whenNotNull { it.asText() }
        )

    private fun buildRequestUrl(request: GooglePlayReviewRequest): String {
        reqId += REQID_INCREMENT
        return REQUEST_URL_DOMAIN +
            REQUEST_URL_PATH +
            REQUEST_URL_PARAMS.format(request.sid, request.bl, request.language.code, reqId)
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
}

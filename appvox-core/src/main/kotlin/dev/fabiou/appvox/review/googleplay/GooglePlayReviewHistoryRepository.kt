package dev.fabiou.appvox.review.googleplay

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.NullNode
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.exception.AppVoxError.INVALID_ARGUMENT
import dev.fabiou.appvox.exception.AppVoxException
import dev.fabiou.appvox.review.ReviewRepository
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.ReviewResult
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewHistoryRequestParameters
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewHistoryResult
import dev.fabiou.appvox.util.HttpUtil
import dev.fabiou.appvox.util.JsonUtil.getJsonNodeByIndex

internal class GooglePlayReviewHistoryRepository(
    private val config: RequestConfiguration
) : ReviewRepository<GooglePlayReviewHistoryRequestParameters, GooglePlayReviewHistoryResult> {

    companion object {
        internal var REQUEST_URL_DOMAIN = "https://play.google.com"
        internal const val REQUEST_URL_PATH = "/_/PlayStoreUi/data/batchexecute"
        private const val REQUEST_URL_PARAMS = "?rpcids=UsvDTd" +
            "&f.sid=%s" +
            "&bl=%s" +
            "&hl=%s" +
            "&gl=US" +
            "&authuser=0" +
            "&soc-app=121" +
            "&soc-platform=1" +
            "&soc-device=1" +
            "&_reqid=%s"

        private const val REQUEST_BODY =
            "f.req=[[[\"UsvDTd\",\"[null,null,[4,null,[%d]],[\\\"%s\\\",7],\\\"%s\\\"]\",null,\"1\"]]]"

        private val ROOT_ARRAY_INDEX = intArrayOf(0, 2)

        private val REVIEW_ID_INDEX = intArrayOf(0)
        private val USER_NAME_INDEX = intArrayOf(1, 0)
        private val USER_PROFILE_PIC_INDEX = intArrayOf(1, 1, 3, 2)
        private val RATING_INDEX = intArrayOf(2)
        private val COMMENT_INDEX = intArrayOf(4)
        private val SUBMIT_TIME_INDEX = intArrayOf(5, 0)
        private val LIKE_COUNT_INDEX = intArrayOf(6)
        private val APP_VERSION_INDEX = intArrayOf(10)
//        private val REPLY_COMMENT_INDEX = intArrayOf(7, 1)
//        private val REPLY_SUBMIT_TIME_INDEX = intArrayOf(7, 2, 0)

        private const val GOOGLE_PLAY_SUB_RESPONSE_START_INDEX = 4

        private const val MIN_BATCH_SIZE = 1
        private const val MAX_BATCH_SIZE = 150

        private const val MIN_RANDOM_REQID_INIT_NUMBER = 0
        private const val MAX_RANDOM_REQID_INIT_NUMBER = 9999
        private const val REQID_INCREMENT = 100000
    }

    private val httpUtils = HttpUtil

    private var reqId = (MIN_RANDOM_REQID_INIT_NUMBER..MAX_RANDOM_REQID_INIT_NUMBER).random()

    override fun getReviewsByAppId(
        request: ReviewRequest<GooglePlayReviewHistoryRequestParameters>
    ): ReviewResult<GooglePlayReviewHistoryResult> {
        if (request.parameters.batchSize !in MIN_BATCH_SIZE..MAX_BATCH_SIZE) {
            throw AppVoxException(INVALID_ARGUMENT)
        }

        val requestUrl = buildRequestUrl(request)
        val requestBody = buildRequestBody(request)
        val responseContent = httpUtils.postRequest(requestUrl, requestBody, config.proxy)
        val reviews = ArrayList<GooglePlayReviewHistoryResult>()
        val googlePlayResponse = parseReviewsFromResponse(responseContent)
        val googlePlayRawReviews = googlePlayResponse.first()
        for (googlePlayRawReview in googlePlayRawReviews) {
            val review = GooglePlayReviewHistoryResult(
                reviewId = getJsonNodeByIndex(googlePlayRawReview, REVIEW_ID_INDEX).asText(),
                userName = getJsonNodeByIndex(googlePlayRawReview, USER_NAME_INDEX).asText(),
                userProfilePicUrl = getJsonNodeByIndex(googlePlayRawReview, USER_PROFILE_PIC_INDEX).asText(),
                rating = getJsonNodeByIndex(googlePlayRawReview, RATING_INDEX).asInt(),
                comment = getJsonNodeByIndex(googlePlayRawReview, COMMENT_INDEX).asText(),
                submitTime = getJsonNodeByIndex(googlePlayRawReview, SUBMIT_TIME_INDEX).asLong(),
                likeCount = getJsonNodeByIndex(googlePlayRawReview, LIKE_COUNT_INDEX).asInt(),
                appVersion = getJsonNodeByIndex(googlePlayRawReview, APP_VERSION_INDEX).asText(),
            )
            reviews.add(review)
        }

        return ReviewResult(
            results = reviews
        )
    }

    private fun buildRequestUrl(
        request: ReviewRequest<GooglePlayReviewHistoryRequestParameters>
    ): String {
        reqId += REQID_INCREMENT
        return REQUEST_URL_DOMAIN +
            REQUEST_URL_PATH +
            REQUEST_URL_PARAMS.format(
                request.parameters.sid, request.parameters.bl, request.parameters.language.langCode, reqId
            )
    }

    private fun buildRequestBody(
        request: ReviewRequest<GooglePlayReviewHistoryRequestParameters>
    ): String {
        return REQUEST_BODY.format(
                request.parameters.batchSize,
                request.parameters.appId,
                request.parameters.reviewId
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

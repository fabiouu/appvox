package dev.fabiou.appvox.core.review.googleplay

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import dev.fabiou.appvox.core.review.ReviewRepository
import dev.fabiou.appvox.core.review.ReviewResult
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.exception.AppVoxError
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.review.ReviewRequest
import dev.fabiou.appvox.core.review.googleplay.domain.GooglePlayReviewRequest
import dev.fabiou.appvox.core.review.googleplay.domain.GooglePlayReviewResult
import dev.fabiou.appvox.core.util.HttpUtil
import dev.fabiou.appvox.core.util.JsonUtil.getJsonNodeByIndex
import dev.fabiou.appvox.core.util.UrlUtil

internal class GooglePlayReviewRepository(
        private val config: RequestConfiguration
) : ReviewRepository<GooglePlayReviewRequest, GooglePlayReviewResult.GooglePlayReview> {
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

    private var httpUtils = HttpUtil

    @Throws(AppVoxException::class)
    override fun getReviewsByAppId(request: ReviewRequest<GooglePlayReviewRequest>): ReviewResult<GooglePlayReviewResult.GooglePlayReview> {
        if (request.parameters.batchSize !in 1..100) {
            throw AppVoxException(AppVoxError.INVALID_ARGUMENT)
        }

        val requestBody = if (request.nextToken.isNullOrEmpty()) {
            REQUEST_BODY_WITH_PARAMS.format(request.parameters.sortType.sortType, request.parameters.batchSize, request.parameters.appId)
        } else {
            REQUEST_BODY_WITH_PARAMS_AND_BODY.format(request.parameters.batchSize, request.nextToken, request.parameters.appId)
        }

        val requestUrl = UrlUtil.getUrlDomainByEnv(REQUEST_URL_DOMAIN) +
                REQUEST_URL_PATH + REQUEST_URL_PARAMS.format(request.parameters.language.langCode)
        val responseContent = httpUtils.postRequest(requestUrl, requestBody, config?.proxy)

        val reviews = ArrayList<GooglePlayReviewResult.GooglePlayReview>()
        val gplayReviews = parseReviewsFromResponse(responseContent)
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
                            request.parameters.appId, request.parameters.language.langCode, getJsonNodeByIndex(gplayReview, REVIEW_ID_INDEX).asText()),
                    replyComment = getJsonNodeByIndex(gplayReview, REPLY_COMMENT_INDEX).asText(),
                    replySubmitTime = getJsonNodeByIndex(gplayReview, REPLY_SUBMIT_TIME_INDEX).asLong()
            )
            reviews.add(review)
        }

        val token = if (!gplayReviews.isEmpty && !gplayReviews[1].isEmpty) gplayReviews[1][1] else null
        return ReviewResult(
            results = reviews,
            nextToken = token?.asText()
        )
    }

    private fun parseReviewsFromResponse(gPlayResponse: String): JsonNode {
        val cleanResponse = gPlayResponse.substring(4)
        val rootArray = ObjectMapper().readTree(cleanResponse)
        val subArray: JsonNode = rootArray[0][2]
        val subArrayAsJsonString = subArray.textValue()
        val reviews = ObjectMapper().readTree(subArrayAsJsonString)
        return reviews
    }
}
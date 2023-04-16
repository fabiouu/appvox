package io.appvox.googleplay.review

import io.appvox.core.configuration.Constant.MAX_RETRY_ATTEMPTS
import io.appvox.core.configuration.Constant.MIN_RETRY_DELAY
import io.appvox.core.configuration.RequestConfiguration
import io.appvox.core.util.retryRequest
import io.appvox.googleplay.app.GooglePlayRepository
import io.appvox.googleplay.review.domain.GooglePlayReview
import io.appvox.googleplay.review.domain.GooglePlayReviewRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@PublishedApi
internal class GooglePlayReviewService(val config: RequestConfiguration) {

    private val googlePlayRepository = GooglePlayRepository(config)

    private val googlePlayReviewRepository = GooglePlayReviewRepository(config)

    fun getReviewsByAppId(initialRequest: GooglePlayReviewRequest): Flow<GooglePlayReview> = flow {
        var request = initialRequest
        do {
            val response = retryRequest(MAX_RETRY_ATTEMPTS, MIN_RETRY_DELAY) {
                val scriptParameters = googlePlayRepository.memoizedScriptParameters(
                    request.appId,
                    request.language
                )
                val requestWithScriptParams = request.copy(
                    sid = scriptParameters["sid"] ?: error("Failed to extract Google Play sid value"),
                    bl = scriptParameters["bl"] ?: error("Failed to extract Google Play bl value"),
                )
                googlePlayReviewRepository.getReviewsByAppId(requestWithScriptParams)
            }
            request = request.copy(nextToken = response.nextToken)
            response.results?.forEach { result ->
                val review = if (request.fetchHistory && result.hasEditHistory) {
                    delay(timeMillis = config.delay.inWholeMilliseconds)
                    val reviewUserCommentHistory = retryRequest(MAX_RETRY_ATTEMPTS, MIN_RETRY_DELAY) {
                        googlePlayReviewRepository.getReviewCommentHistoryById(result.id, request)
                    }
                    result.copy(userComments = reviewUserCommentHistory)
                } else {
                    result
                }
                emit(review)
            }
            delay(timeMillis = config.delay.inWholeMilliseconds)
        } while (request.nextToken != null)
    }
}

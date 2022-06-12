package io.appvox.googleplay.review

import io.appvox.core.configuration.Constant.MAX_RETRY_ATTEMPTS
import io.appvox.core.configuration.Constant.MIN_RETRY_DELAY
import io.appvox.core.configuration.RequestConfiguration
import io.appvox.core.review.ReviewRequest
import io.appvox.core.review.ReviewService
import io.appvox.core.util.retryRequest
import io.appvox.googleplay.app.GooglePlayRepository
import io.appvox.googleplay.review.domain.GooglePlayReview
import io.appvox.googleplay.review.domain.GooglePlayReviewRequestParameters
import io.appvox.googleplay.review.domain.GooglePlayReviewResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@PublishedApi
internal class GooglePlayReviewService(
    val config: RequestConfiguration
) : ReviewService<GooglePlayReviewRequestParameters, GooglePlayReviewResult, GooglePlayReview> {

    private val googlePlayRepository = GooglePlayRepository(config)

    private val googlePlayReviewRepository = GooglePlayReviewRepository(config)

    private val googlePlayReviewConverter = GooglePlayReviewConverter()

    override fun getReviewsByAppId(
        initialRequest: ReviewRequest<GooglePlayReviewRequestParameters>
    ): Flow<GooglePlayReview> = flow {
        var request = initialRequest
        do {
            val response = retryRequest(MAX_RETRY_ATTEMPTS, MIN_RETRY_DELAY) {
                val scriptParameters = googlePlayRepository.memoizedScriptParameters(
                    request.parameters.appId,
                    request.parameters.language
                )

                val requestCopy = request.copy(
                    parameters = request.parameters.copy(
                        appId = request.parameters.appId,
                        language = request.parameters.language,
                        sortType = request.parameters.sortType,
                        rating = request.parameters.rating,
                        fetchHistory = request.parameters.fetchHistory,
                        batchSize = request.parameters.batchSize,
                        deviceName = request.parameters.deviceName,
                        sid = scriptParameters["sid"] ?: error("Failed to extract Google Play sid value"),
                        bl = scriptParameters["bl"] ?: error("Failed to extract Google Play bl value"),
                    ),
                    nextToken = request.nextToken
                )

                googlePlayReviewRepository.getReviewsByAppId(requestCopy)
            }
            request = request.copy(request.parameters, response.nextToken)
            response.results?.forEach { result ->
                val review = if (request.parameters.fetchHistory &&
                    result.hasEditHistory != null && result.hasEditHistory
                ) {
                    delay(timeMillis = config.delay.toLong())
                    val reviewHistoryResult = retryRequest(MAX_RETRY_ATTEMPTS, MIN_RETRY_DELAY) {
                        googlePlayReviewRepository.getReviewHistoryById(result.reviewId, request)
                    }
                    googlePlayReviewConverter.toResponseWithHistory(request.parameters, result, reviewHistoryResult)
                } else {
                    googlePlayReviewConverter.toResponse(request.parameters, result)
                }
                emit(review)
            }
            delay(timeMillis = config.delay.toLong())
        } while (request.nextToken != null)
    }
}

package dev.fabiou.appvox.review.googleplay

import dev.fabiou.appvox.app.googleplay.GooglePlayRepository
import dev.fabiou.appvox.configuration.Constant.MAX_RETRY_ATTEMPTS
import dev.fabiou.appvox.configuration.Constant.MIN_RETRY_DELAY
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.ReviewService
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewRequestParameters
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewResult
import dev.fabiou.appvox.util.retryRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class GooglePlayReviewService(
    val config: RequestConfiguration
) : ReviewService<GooglePlayReviewRequestParameters, GooglePlayReviewResult, GooglePlayReview> {

    private val googlePlayRepository = GooglePlayRepository(config)

    private val googlePlayReviewRepository = GooglePlayReviewRepository(config)

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
                        sid = scriptParameters["sid"] ?: error("Failed to extract Google Play sid value"),
                        bl = scriptParameters["bl"] ?: error("Failed to extract Google Play bl value"),
                    ),
                    nextToken = request.nextToken
                )

                googlePlayReviewRepository.getReviewsByAppId(requestCopy)
            }
            request = request.copy(request.parameters, response.nextToken)
            response.results.forEach { result ->
                val review = if (request.parameters.fetchHistory) {
                    delay(timeMillis = config.delay.toLong())
                    val reviewHistoryResult = retryRequest(MAX_RETRY_ATTEMPTS, MIN_RETRY_DELAY) {
                        googlePlayReviewRepository.getReviewHistoryById(result.reviewId, request)
                    }
//                    val userTypes = GooglePlayReviewClassificationService().defineUserPersona(reviewHistoryResult)
                    GooglePlayReviewConverter().toResponseWithHistory(result, reviewHistoryResult, emptySet())
                } else {
//                    val userTypes = GooglePlayReviewClassificationService().classify(result)
                    GooglePlayReviewConverter().toResponse(result)
                }
                emit(review)
            }
            delay(timeMillis = config.delay.toLong())
        } while (request.nextToken != null)
    }
}

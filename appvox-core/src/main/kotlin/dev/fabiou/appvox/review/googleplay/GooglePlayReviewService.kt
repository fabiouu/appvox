package dev.fabiou.appvox.review.googleplay

import dev.fabiou.appvox.app.googleplay.GooglePlayRepository
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.ReviewResult
import dev.fabiou.appvox.review.ReviewService
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewRequestParameters
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewResult
import dev.fabiou.appvox.util.retryRequest

internal class GooglePlayReviewService(
    val config: RequestConfiguration
) : ReviewService<GooglePlayReviewRequestParameters, GooglePlayReviewResult> {

    companion object {
        private const val MIN_RETRY_DELAY = 3000L
        private const val MAX_RETRY_ATTEMPTS = 5
    }

    private val googlePlayReviewRepository = GooglePlayReviewRepository(config)

    private val googlePlayRepository = GooglePlayRepository(config)

    override suspend fun getReviewsByAppId(
        request: ReviewRequest<GooglePlayReviewRequestParameters>
    ): ReviewResult<GooglePlayReviewResult> {

        val scriptParameters = googlePlayRepository.memoizedScriptParameters(
            request.parameters.appId,
            request.parameters.language
        )

        val requestCopy = request.copy(
            parameters = request.parameters.copy(
                appId = request.parameters.appId,
                language = request.parameters.language,
                sortType = request.parameters.sortType,
                batchSize = request.parameters.batchSize,
                sid = scriptParameters["sid"] ?: error("Failed to extract Google Play sid value"),
                bl = scriptParameters["bl"] ?: error("Failed to extract Google Play bl value"),
            ),
            nextToken = request.nextToken
        )

        return retryRequest(maxAttempts = MAX_RETRY_ATTEMPTS, minRetryDelay = MIN_RETRY_DELAY) {
            googlePlayReviewRepository.getReviewsByAppId(requestCopy)
        }
    }
}

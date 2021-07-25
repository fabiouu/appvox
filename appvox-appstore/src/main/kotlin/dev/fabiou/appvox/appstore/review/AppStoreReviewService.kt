package dev.fabiou.appvox.appstore.review

import dev.fabiou.appvox.appstore.app.AppStoreRepository
import dev.fabiou.appvox.appstore.review.domain.AppStoreReview
import dev.fabiou.appvox.appstore.review.domain.AppStoreReviewRequestParameters
import dev.fabiou.appvox.appstore.review.domain.AppStoreReviewResult
import dev.fabiou.appvox.configuration.Constant
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.ReviewService
import dev.fabiou.appvox.util.retryRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class AppStoreReviewService(
    val config: RequestConfiguration
) : ReviewService<AppStoreReviewRequestParameters, AppStoreReviewResult.AppStoreReview, AppStoreReview> {

    private val appStoreRepository = AppStoreRepository(config)

    private val appStoreReviewRepository = AppStoreReviewRepository(config)

    private val appStoreReviewConverter = AppStoreReviewConverter()

    override fun getReviewsByAppId(
        initialRequest: ReviewRequest<AppStoreReviewRequestParameters>
    ): Flow<AppStoreReview> = flow {
        var request = initialRequest
        do {
            val response = retryRequest(Constant.MAX_RETRY_ATTEMPTS, Constant.MIN_RETRY_DELAY) {
                val bearerToken = appStoreRepository.memoizedBearerToken(
                    request.parameters.appId,
                    request.parameters.region
                )
                val requestCopy = request.copy(
                    parameters = request.parameters.copy(
                        appId = request.parameters.appId,
                        region = request.parameters.region,
                        bearerToken = bearerToken
                    ),
                    nextToken = request.nextToken
                )
                appStoreReviewRepository.getReviewsByAppId(requestCopy)
            }
            request = request.copy(request.parameters, response.nextToken)
            response.results.forEach { result ->
                val review = appStoreReviewConverter.toResponse(request.parameters, result)
                emit(review)
            }
            delay(timeMillis = config.delay.toLong())
        } while (request.nextToken != null)
    }
}

package io.appvox.appstore.review

import io.appvox.appstore.app.AppStoreRepository
import io.appvox.appstore.review.domain.AppStoreReview
import io.appvox.appstore.review.domain.AppStoreReviewRequestParameters
import io.appvox.appstore.review.domain.AppStoreReviewResult
import io.appvox.core.configuration.Constant
import io.appvox.core.configuration.RequestConfiguration
import io.appvox.core.review.ReviewRequest
import io.appvox.core.review.ReviewService
import io.appvox.core.util.retryRequest
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
            response.results?.forEach { result ->
                val review = appStoreReviewConverter.toResponse(request.parameters, result)
                emit(review)
            }
            delay(timeMillis = config.delay.toLong())
        } while (request.nextToken != null)
    }
}

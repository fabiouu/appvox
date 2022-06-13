package io.appvox.appstore.review

import io.appvox.appstore.review.domain.AppStoreReview
import io.appvox.appstore.review.domain.AppStoreReviewRequestParameters
import io.appvox.appstore.review.domain.AppStoreReviewResult
import io.appvox.core.configuration.Constant.MAX_RETRY_ATTEMPTS
import io.appvox.core.configuration.Constant.MIN_RETRY_DELAY
import io.appvox.core.configuration.RequestConfiguration
import io.appvox.core.review.ReviewRequest
import io.appvox.core.review.ReviewService
import io.appvox.core.util.retryRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class AppStoreReviewService(
    private val config: RequestConfiguration
) : ReviewService<AppStoreReviewRequestParameters, AppStoreReviewResult.Entry, AppStoreReview> {

    private val appStoreReviewRepository = AppStoreReviewRepository(config)

    private val appStoreReviewConverter = AppStoreReviewConverter()

    override fun getReviewsByAppId(
        initialRequest: ReviewRequest<AppStoreReviewRequestParameters>
    ): Flow<AppStoreReview> = flow {
        var request = initialRequest
        do {
            val response = retryRequest(MAX_RETRY_ATTEMPTS, MIN_RETRY_DELAY) {
                appStoreReviewRepository.getReviewsByAppId(request)
            }
            request = request.copy(request.parameters, response.nextToken)
            response.results?.forEach { result ->
                val review = appStoreReviewConverter.toResponse(request.parameters, result)
                emit(review)
            }
            delay(timeMillis = config.delay.toLong())
        } while (request.nextToken != null && response.results != null)
    }
}

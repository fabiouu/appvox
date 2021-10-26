package io.appvox.appstore.review

import io.appvox.appstore.review.domain.ItunesRssReview
import io.appvox.appstore.review.domain.ItunesRssReviewRequestParameters
import io.appvox.appstore.review.domain.ItunesRssReviewResult
import io.appvox.core.configuration.Constant.MAX_RETRY_ATTEMPTS
import io.appvox.core.configuration.Constant.MIN_RETRY_DELAY
import io.appvox.core.configuration.RequestConfiguration
import io.appvox.core.review.ReviewRequest
import io.appvox.core.review.ReviewService
import io.appvox.core.util.retryRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class ItunesRssReviewService(
    val config: RequestConfiguration
) : ReviewService<ItunesRssReviewRequestParameters, ItunesRssReviewResult.Entry, ItunesRssReview> {

    private val itunesRssReviewRepository = ItunesRssReviewRepository(config)

    private val itunesRssReviewConverter = ItunesRssReviewConverter()

    override fun getReviewsByAppId(
        initialRequest: ReviewRequest<ItunesRssReviewRequestParameters>
    ): Flow<ItunesRssReview> = flow {
        var request = initialRequest
        do {
            val response = retryRequest(MAX_RETRY_ATTEMPTS, MIN_RETRY_DELAY) {
                itunesRssReviewRepository.getReviewsByAppId(request)
            }
            request = request.copy(request.parameters, response.nextToken)
            response.results?.forEach { result ->
                val review = itunesRssReviewConverter.toResponse(request.parameters, result)
                emit(review)
            }
            delay(timeMillis = config.delay.toLong())
        } while (request.nextToken != null)
    }
}

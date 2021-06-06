package dev.fabiou.appvox.review.itunesrss

import dev.fabiou.appvox.configuration.Constant.MAX_RETRY_ATTEMPTS
import dev.fabiou.appvox.configuration.Constant.MIN_RETRY_DELAY
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.ReviewService
import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReview
import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReviewRequestParameters
import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReviewResult
import dev.fabiou.appvox.util.retryRequest
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
            response.results.forEach { result ->
                val review = itunesRssReviewConverter.toResponse(request.parameters, result)
                emit(review)
            }
            delay(timeMillis = config.delay.toLong())
        } while (request.nextToken != null)
    }
}

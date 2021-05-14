package dev.fabiou.appvox

import dev.fabiou.appvox.configuration.Constant
import dev.fabiou.appvox.configuration.Constant.MIN_REQUEST_DELAY
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.exception.AppVoxError
import dev.fabiou.appvox.exception.AppVoxException
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.itunesrss.ItunesRssReviewConverter
import dev.fabiou.appvox.review.itunesrss.ItunesRssReviewService
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion
import dev.fabiou.appvox.review.itunesrss.constant.ItunesRssSortType
import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReview
import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReviewRequestParameters
import dev.fabiou.appvox.util.HttpUtil
import dev.fabiou.appvox.util.retryRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * This class consists of the main methods for interacting with iTunes RSS feed
 */
class ItunesRss(
    val config: RequestConfiguration = RequestConfiguration(delay = MIN_REQUEST_DELAY)
) {
    private val itunesRssReviewService = ItunesRssReviewService(config)

    private val itunesRssReviewConverter = ItunesRssReviewConverter()

    init {
        config.proxyAuthentication?.let { it ->
            HttpUtil.setAuthenticator(it.userName, it.password)
        }
    }

    /**
     * Returns a flow of Reviews from iTunes RSS Feed
     */
    fun reviews(
        appId: String,
        region: AppStoreRegion = AppStoreRegion.UNITED_STATES,
        sortType: ItunesRssSortType = ItunesRssSortType.RELEVANT
    ): Flow<ItunesRssReview> = flow {

        if (config.delay < MIN_REQUEST_DELAY) {
            throw AppVoxException(AppVoxError.REQ_DELAY_TOO_SHORT)
        }

        val initialRequest = ReviewRequest(ItunesRssReviewRequestParameters(appId, region, sortType))
        var request = initialRequest
        do {
            val response = retryRequest(Constant.MAX_RETRY_ATTEMPTS, Constant.MIN_RETRY_DELAY) {
                itunesRssReviewService.getReviewsByAppId(request)
            }
            request = request.copy(request.parameters, response.nextToken)
            val reviews = itunesRssReviewConverter.toResponse(response.results)
            reviews.forEach { review ->
                emit(review)
            }
            delay(timeMillis = config.delay.toLong())
        } while (request.nextToken != null)
    }
}

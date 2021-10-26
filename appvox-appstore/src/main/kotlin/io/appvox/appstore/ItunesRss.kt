package io.appvox.appstore

import io.appvox.appstore.review.ItunesRssReviewService
import io.appvox.appstore.review.domain.ItunesRssReview
import io.appvox.appstore.review.domain.ItunesRssReviewRequestParameters
import io.appvox.core.configuration.Constant.MIN_REQUEST_DELAY
import io.appvox.core.configuration.RequestConfiguration
import io.appvox.core.exception.AppVoxError
import io.appvox.core.exception.AppVoxException
import io.appvox.core.review.ReviewRequest
import io.appvox.core.util.HttpUtil
import kotlinx.coroutines.flow.Flow

/**
 * This class consists of the main methods for interacting with iTunes RSS feed
 */
class ItunesRss(
    val config: RequestConfiguration = RequestConfiguration(delay = MIN_REQUEST_DELAY)
) {
    private val itunesRssReviewService = ItunesRssReviewService(config)

    init {
        config.proxyAuthentication?.let { it ->
            HttpUtil.setAuthenticator(it.userName, it.password)
        }
    }

    /**
     * Returns a flow of Reviews from iTunes RSS Feed
     */
    fun reviews(parameters: ItunesRssReviewRequestParameters.Builder.() -> Unit): Flow<ItunesRssReview> {
        if (config.delay < MIN_REQUEST_DELAY) {
            throw AppVoxException(AppVoxError.REQ_DELAY_TOO_SHORT)
        }
        val itunesRssRequest = ItunesRssReviewRequestParameters.Builder().apply(parameters).build()
        val initialRequest = ReviewRequest(itunesRssRequest)
        return itunesRssReviewService.getReviewsByAppId(initialRequest)
    }
}

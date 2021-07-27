package io.appvox.appstore

import io.appvox.appstore.review.ItunesRssReviewService
import io.appvox.appstore.review.constant.AppStoreRegion
import io.appvox.appstore.review.constant.ItunesRssSortType
import io.appvox.appstore.review.domain.AppStoreReviewRequestParameters
import io.appvox.appstore.review.domain.ItunesRssReview
import io.appvox.appstore.review.domain.ItunesRssReviewRequestParameters
import io.appvox.configuration.Constant.MIN_REQUEST_DELAY
import io.appvox.configuration.RequestConfiguration
import io.appvox.exception.AppVoxError
import io.appvox.exception.AppVoxException
import io.appvox.review.ReviewRequest
import io.appvox.util.HttpUtil
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

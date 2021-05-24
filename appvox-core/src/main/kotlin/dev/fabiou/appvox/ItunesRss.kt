package dev.fabiou.appvox

import dev.fabiou.appvox.configuration.Constant.MIN_REQUEST_DELAY
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.exception.AppVoxError
import dev.fabiou.appvox.exception.AppVoxException
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.itunesrss.ItunesRssReviewService
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion
import dev.fabiou.appvox.review.itunesrss.constant.ItunesRssSortType
import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReview
import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReviewRequestParameters
import dev.fabiou.appvox.util.HttpUtil
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
    fun reviews(
        appId: String,
        region: AppStoreRegion = AppStoreRegion.UNITED_STATES,
        sortType: ItunesRssSortType = ItunesRssSortType.RELEVANT
    ): Flow<ItunesRssReview> {
        if (config.delay < MIN_REQUEST_DELAY) {
            throw AppVoxException(AppVoxError.REQ_DELAY_TOO_SHORT)
        }
        val initialRequest = ReviewRequest(ItunesRssReviewRequestParameters(appId, region, sortType))
        return itunesRssReviewService.getReviewsByAppId(initialRequest)
    }
}

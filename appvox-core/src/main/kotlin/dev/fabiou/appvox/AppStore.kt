package dev.fabiou.appvox

import dev.fabiou.appvox.appstore.review.service.AppStoreReviewService
import dev.fabiou.appvox.configuration.Constant.MIN_REQUEST_DELAY
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.exception.AppVoxError
import dev.fabiou.appvox.exception.AppVoxException
import dev.fabiou.appvox.review.ReviewIterator
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.appstore.AppStoreReviewConverter
import dev.fabiou.appvox.review.appstore.domain.AppStoreReview
import dev.fabiou.appvox.review.appstore.domain.AppStoreReviewRequest
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion
import dev.fabiou.appvox.util.HttpUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * This class consists of the main methods for interacting with Apple App Store
 *
 * @property config
 * @constructor Create empty App store
 */
class AppStore(
    val config: RequestConfiguration = RequestConfiguration(delay = MIN_REQUEST_DELAY)
) {
    private val appStoreReviewService = AppStoreReviewService(config)

    private val appStoreReviewConverter = AppStoreReviewConverter()

    init {
        config.proxyAuthentication?.let { it ->
            HttpUtil.setAuthenticator(it.userName, it.password)
        }
    }

    /**
     * Returns a flow of Reviews from iTunes RSS Feed
     *
     * @param appId
     * @param region
     * @return
     */
    fun reviews(
        appId: String,
        region: AppStoreRegion = AppStoreRegion.UNITED_STATES
    ): Flow<AppStoreReview> = flow {

        if (config.delay < MIN_REQUEST_DELAY) {
            throw AppVoxException(AppVoxError.REQ_DELAY_TOO_SHORT)
        }

        val iterator = ReviewIterator(
            converter = appStoreReviewConverter,
            service = appStoreReviewService,
            request = ReviewRequest(
                AppStoreReviewRequest(
                    appId = appId,
                    region = region
                )
            )
        )

        iterator.forEach { reviews ->
            reviews.forEach { review ->
                delay(config.delay.toLong())
                emit(review)
            }
        }
    }
}

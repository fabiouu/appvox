package com.appvox.core.query

import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.constant.GooglePlaySortType
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import com.appvox.core.review.facade.AppStoreReviewFacade
import com.appvox.core.review.facade.GooglePlayReviewFacade
import com.appvox.core.review.iterator.AppStoreReviewIterator
import com.appvox.core.review.iterator.GooglePlayReviewIterator

class AppVox(
    private val config: ProxyConfiguration? = null
) {
    fun appStoreReviews(appId: String, region: String) : AppStoreReviewIterator {
        val facade = AppStoreReviewFacade(configuration = config)
        val request = AppStoreReviewRequest(region = region)
        return AppStoreReviewIterator(facade, appId, request)
    }

    fun googlePlayReviews(appId: String, language: String, sortType: GooglePlaySortType, batchSize: Int) : GooglePlayReviewIterator {
        val facade = GooglePlayReviewFacade(configuration = config)
        val request = GooglePlayReviewRequest(
            language = language,
            sortType = sortType,
            batchSize = batchSize
        )
        return GooglePlayReviewIterator(facade, appId, request)
    }
}
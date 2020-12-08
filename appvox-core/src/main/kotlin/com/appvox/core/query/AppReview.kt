package com.appvox.core.query

import com.appvox.core.configuration.Configuration
import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.constant.GooglePlayLanguage
import com.appvox.core.review.constant.GooglePlaySortType
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import com.appvox.core.review.facade.AppStoreReviewFacade
import com.appvox.core.review.facade.GooglePlayReviewFacade
import com.appvox.core.review.iterator.AppStoreReviewIterator
import com.appvox.core.review.iterator.GooglePlayReviewIterator

class AppReview(
    private val configuration: Configuration = Configuration()
) {
    fun appStore(appId: String, region: String, fetchCountlimit: Int = 0) : AppStoreReviewIterator {
        val facade = AppStoreReviewFacade(configuration)
        val request = AppStoreReviewRequest(
                region = region,
                fetchCountLimit = fetchCountlimit
        )
        return AppStoreReviewIterator(facade, appId, request)
    }

    fun googlePlay(
            appId: String,
            language: GooglePlayLanguage,
            sortType: GooglePlaySortType = GooglePlaySortType.NEWEST,
            fetchCountlimit: Int = 0,
            batchSize: Int = 40) : GooglePlayReviewIterator {
        val facade = GooglePlayReviewFacade(configuration)
        val request = GooglePlayReviewRequest(
            language = language,
            sortType = sortType,
            fetchCountLimit = fetchCountlimit,
            batchSize = batchSize
        )
        return GooglePlayReviewIterator(facade, appId, request)
    }
}
package com.appvox.core.query

import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.facade.AppStoreReviewFacade
import com.appvox.core.review.facade.GooglePlayReviewFacade
import com.appvox.core.review.iterator.AppStoreReviewIterator
import com.appvox.core.review.iterator.GooglePlayReviewIterator

class AppVox {

    lateinit var config: ProxyConfiguration

    constructor(config : ProxyConfiguration) {
        this.config = config
    }

    constructor() {
    }

    fun appStoreReviews(appId: String) : AppStoreReviewIterator {
        val facade = AppStoreReviewFacade(config)
        return AppStoreReviewIterator(facade, appId)
    }

    fun googlePlayReviews(appId: String) : GooglePlayReviewIterator {
        val facade = GooglePlayReviewFacade(config)
        return GooglePlayReviewIterator(facade, appId)
    }

}
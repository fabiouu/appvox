package com.appvox.core.query

import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.iterator.AppReviewIterator
import com.appvox.core.review.facade.AppStoreReviewFacade

class AppVox {

    lateinit var config: ProxyConfiguration

    constructor(config : ProxyConfiguration) {
        this.config = config
    }

    constructor() {
    }

    fun reviews(appId:String) : AppReviewIterator {
        val facade = AppStoreReviewFacade(config)
        return AppReviewIterator(facade, appId)
    }
}
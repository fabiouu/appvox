package io.appvox.appstore.review.domain

import io.appvox.appstore.review.constant.AppStoreRegion
import io.appvox.appstore.review.constant.ItunesRssSortType

data class ItunesRssReviewRequestParameters(
    val appId: String,
    val region: AppStoreRegion,
    val sortType: ItunesRssSortType,
    val pageNo: Int = 1
) {
    private constructor(builder: Builder) : this(
        appId = builder.appId,
        region = builder.region,
        sortType = ItunesRssSortType.RECENT
    )

    class Builder {
        lateinit var appId: String
        var region: AppStoreRegion = AppStoreRegion.UNITED_STATES
        var sortType: ItunesRssSortType = ItunesRssSortType.RECENT

        fun build() = ItunesRssReviewRequestParameters(this)
    }
}

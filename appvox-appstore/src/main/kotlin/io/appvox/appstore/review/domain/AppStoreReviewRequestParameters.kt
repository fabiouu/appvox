package io.appvox.appstore.review.domain

import io.appvox.appstore.review.constant.AppStoreRegion
import io.appvox.appstore.review.constant.AppStoreSortType

data class AppStoreReviewRequestParameters(
    val appId: String,
    val region: AppStoreRegion,
    val sortType: AppStoreSortType,
    val pageNo: Int = 1
) {
    private constructor(builder: Builder) : this(
        appId = builder.appId,
        region = builder.region,
        sortType = AppStoreSortType.RECENT
    )

    class Builder {
        lateinit var appId: String
        var region: AppStoreRegion = AppStoreRegion.UNITED_STATES
        var sortType: AppStoreSortType = AppStoreSortType.RECENT

        fun build() = AppStoreReviewRequestParameters(this)
    }
}

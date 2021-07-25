package dev.fabiou.appvox.appstore.review.domain

import dev.fabiou.appvox.appstore.review.constant.AppStoreRegion
import dev.fabiou.appvox.appstore.review.constant.ItunesRssSortType

internal data class ItunesRssReviewRequestParameters(
    val appId: String,
    val region: AppStoreRegion,
    val sortType: ItunesRssSortType = ItunesRssSortType.RECENT,
    val pageNo: Int = 1
)

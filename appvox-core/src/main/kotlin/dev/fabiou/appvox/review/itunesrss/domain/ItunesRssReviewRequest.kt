package dev.fabiou.appvox.review.itunesrss.domain

import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion
import dev.fabiou.appvox.review.itunesrss.constant.ItunesRssSortType

internal data class ItunesRssReviewRequest(
    val appId: String,
    val region: AppStoreRegion,
    val sortType: ItunesRssSortType = ItunesRssSortType.RECENT,
    val pageNo: Int = 1
)

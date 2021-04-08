package dev.fabiou.appvox.core.review.itunesrss.domain

import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreRegion
import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreSortType

internal data class ItunesRssReviewRequest(
    val appId: String,
    val region: AppStoreRegion,
    val sortType: AppStoreSortType = AppStoreSortType.RECENT,
    val pageNo: Int = 1
)

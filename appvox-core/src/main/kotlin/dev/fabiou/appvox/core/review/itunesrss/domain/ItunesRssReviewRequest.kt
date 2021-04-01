package dev.fabiou.appvox.core.review.itunesrss.domain

import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreSortType

internal data class ItunesRssReviewRequest(
    val appId: String,
    var nextToken: String? = null,
    val region: String,
    val sortType: AppStoreSortType = AppStoreSortType.RECENT,
    var pageNo: Int = 1
)
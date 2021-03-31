package dev.fabiou.appvox.core.review.itunesrss.domain

import dev.fabiou.appvox.core.review.itunesrss.AppStoreSortType

internal data class ItunesRssReviewRequest(
        val appId: String,
        val maxCount: Int = Int.MAX_VALUE,
        var nextToken: String? = null,
        val region: String,
        val sortType: AppStoreSortType = AppStoreSortType.RECENT,
        var pageNo: Int = 1
)
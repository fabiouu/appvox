package dev.fabiou.appvox.core.appstore.review.domain

import dev.fabiou.appvox.core.appstore.review.constant.AppStoreSortType

 class ItunesRssReviewRequest(
        val appId: String,
        val maxCount: Int = Int.MAX_VALUE,
        var nextToken: String? = null,
        val region: String,
        val sortType: AppStoreSortType = AppStoreSortType.RECENT,
        var pageNo: Int = 1
)
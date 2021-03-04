package dev.fabiou.appvox.core.review.domain.request

import dev.fabiou.appvox.core.review.constant.AppStoreSortType

class AppStoreReviewRequest(
    val appId: String,
    val region: String,
    val sortType: AppStoreSortType = AppStoreSortType.RECENT,
    var pageNo: Int = 1,
    val maxCount: Int = Int.MAX_VALUE,
    var bearerToken: String? = null,
    var nextToken: String? = null
)
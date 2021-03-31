package dev.fabiou.appvox.core.review.appstore.domain

import dev.fabiou.appvox.core.appstore.review.constant.AppStoreSortType

internal data class AppStoreReviewRequest(
    val appId: String,
    val region: String,
    val sortType: AppStoreSortType = AppStoreSortType.RECENT,
    var bearerToken: String? = null,
    val maxCount: Int = Int.MAX_VALUE,
    var nextToken: String? = null
)
package dev.fabiou.appvox.core.review.appstore.domain

import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreRegion

internal data class AppStoreReviewRequest(
    val appId: String,
    val region: AppStoreRegion,
    var bearerToken: String? = null,
    val maxCount: Int = Int.MAX_VALUE,
    var nextToken: String? = null
)

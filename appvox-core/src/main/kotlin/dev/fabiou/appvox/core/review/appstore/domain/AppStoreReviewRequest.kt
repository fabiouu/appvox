package dev.fabiou.appvox.core.review.appstore.domain

import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreRegion

internal data class AppStoreReviewRequest(
    val appId: String,
    val region: AppStoreRegion,
    val bearerToken: String? = null,
)

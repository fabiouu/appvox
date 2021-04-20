package dev.fabiou.appvox.review.appstore.domain

import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion

internal data class AppStoreReviewRequest(
    val appId: String,
    val region: AppStoreRegion,
    val bearerToken: String? = null,
)

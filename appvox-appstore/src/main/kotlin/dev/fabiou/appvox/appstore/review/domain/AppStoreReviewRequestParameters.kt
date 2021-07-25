package dev.fabiou.appvox.appstore.review.domain

import dev.fabiou.appvox.appstore.review.constant.AppStoreRegion

internal data class AppStoreReviewRequestParameters(
    val appId: String,
    val region: AppStoreRegion,
    val bearerToken: String? = null,
)

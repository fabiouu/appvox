package dev.fabiou.appvox.core.review.appstore.domain

internal data class AppStoreReviewRequest(
    val appId: String,
    val region: String,
    var bearerToken: String? = null,
    val maxCount: Int = Int.MAX_VALUE,
    var nextToken: String? = null
)
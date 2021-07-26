package io.appvox.appstore.review.domain

import io.appvox.appstore.review.constant.AppStoreRegion

data class AppStoreReviewRequestParameters(
    val appId: String,
    val region: AppStoreRegion,
    val bearerToken: String? = null,
) {
    private constructor(builder: Builder) : this(
        appId = builder.appId,
        region = builder.region
    )

    class Builder {
        lateinit var appId: String
        var region: AppStoreRegion = AppStoreRegion.UNITED_STATES

        fun build() = AppStoreReviewRequestParameters(this)
    }
}

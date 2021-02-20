package dev.fabiou.appvox.core.review.domain.request

import dev.fabiou.appvox.core.review.constant.AppStoreSortType

class AppStoreReviewRequest(
        val region: String,
        val sortType: AppStoreSortType,
        var bearerToken: String? = null,
        var pageNo: Int = 1,
        val maxCount: Int = 0,
        var nextToken: String? = null
)
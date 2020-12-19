package com.appvox.core.review.domain.request

import com.appvox.core.review.constant.AppStoreSortType

class AppStoreReviewRequest(
    val region: String,
    val sortType: AppStoreSortType,
    var bearerToken: String? = null,
    var pageNo: Int = 1,
    val countLimit: Int = 0,
    var nextToken: String? = null
)
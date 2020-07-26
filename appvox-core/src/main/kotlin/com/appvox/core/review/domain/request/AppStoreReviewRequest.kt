package com.appvox.core.review.domain.request

import com.appvox.core.PaginatedRequest

open class AppStoreReviewRequest(
    val region: String = "us",
    val size: Int = 0,
    val next: String?,
    cursor : String? = null
) : PaginatedRequest(cursor)
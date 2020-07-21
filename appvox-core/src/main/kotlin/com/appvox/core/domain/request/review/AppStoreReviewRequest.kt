package com.appvox.core.domain.request.review

import com.appvox.core.domain.request.PaginatedRequest

open class AppStoreReviewRequest(
    val region: String = "us",
    val size: Int = 0,
    val next: String?,
    cursor : String? = null
) : PaginatedRequest(cursor)
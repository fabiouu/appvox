package com.appvox.appvox.domain.request.review

import com.appvox.appvox.domain.request.PaginatedRequest

open class AppStoreReviewRequest(
    val region: String = "us",
    val size: Int = 0,
    val next: String?,
    cursor : String?
) : PaginatedRequest(cursor)
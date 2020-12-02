package com.appvox.core.review.domain.request

import com.appvox.core.PaginatedRequest

open class AppStoreReviewRequest(
    val region: String,
    val bearerToken: String,
    val next: String?,
    cursor : String? = null
) : PaginatedRequest(cursor)
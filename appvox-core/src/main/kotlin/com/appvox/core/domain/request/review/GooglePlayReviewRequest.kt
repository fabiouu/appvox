package com.appvox.core.domain.request.review

import com.appvox.core.domain.request.PaginatedRequest

open class GooglePlayReviewRequest(
        val language: String = "en",
        val sort: Int = 1,
        val size: Int = 40,
        val token: String? = "",
        cursor: String? = null
) : PaginatedRequest(cursor)
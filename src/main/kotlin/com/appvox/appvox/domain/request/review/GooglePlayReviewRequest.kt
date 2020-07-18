package com.appvox.appvox.domain.request.review

import com.appvox.appvox.domain.request.PaginatedRequest

open class GooglePlayReviewRequest(
        val language: String = "en",
        val sort: Int = 1,
        val size: Int = 40,
        val token: String? = "",
        cursor: String?
) : PaginatedRequest(cursor)
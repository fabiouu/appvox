package com.appvox.core.review.domain.request

import com.appvox.core.PaginatedRequest

open class GooglePlayReviewRequest(
        val language: String,
        val sortType: Int,
        val size: Int,
        var nextToken: String? = null
//        cursor: String? = null
)// : PaginatedRequest(cursor)
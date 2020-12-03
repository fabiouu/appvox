package com.appvox.core.review.domain.request

import com.appvox.core.PaginatedRequest
import com.appvox.core.review.constant.GooglePlaySortType

open class GooglePlayReviewRequest(
    val language: String,
    val sortType: GooglePlaySortType,
    val batchSize: Int,
    var nextToken: String? = null
//        cursor: String? = null
)// : PaginatedRequest(cursor)
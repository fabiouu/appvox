package com.appvox.core.review.domain.request

import com.appvox.core.PaginatedRequest

 class AppStoreReviewRequest(
     val region: String,
     var bearerToken: String? = null,
     var nextToken: String? = null
//    cursor : String? = null
)// : PaginatedRequest(cursor)
package com.appvox.core.review.domain.response

import com.appvox.core.PaginatedResponse

open class ReviewsResponse(
    val reviews: List<ReviewDTO>,
    nextCursor: String? = null
) : PaginatedResponse(nextCursor)
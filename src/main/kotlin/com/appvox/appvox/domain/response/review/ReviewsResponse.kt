package com.appvox.appvox.domain.response.review

import com.appvox.appvox.domain.response.PaginatedResponse

open class ReviewsResponse(
        val reviews: List<ReviewResponse>,
        nextCursor: String? = null
) : PaginatedResponse(nextCursor)
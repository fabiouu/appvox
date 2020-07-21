package com.appvox.core.domain.response.review

import com.appvox.core.domain.response.PaginatedResponse

open class ReviewsResponse(
        val reviews: List<ReviewResponse>,
        nextCursor: String? = null
) : PaginatedResponse(nextCursor)
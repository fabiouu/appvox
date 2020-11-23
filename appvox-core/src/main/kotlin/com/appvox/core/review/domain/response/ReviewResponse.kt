package com.appvox.core.review.domain.response

import com.appvox.core.PaginatedResponse

open class ReviewResponse(
        val reviews: List<Review>,
        nextCursor: String? = null
) : PaginatedResponse(nextCursor) {
    data class Review(
            val type: String,
            val id: String,
            val userName: String,
            val userProfile: String? = null,
            val rating: Int,
            val title: String? = null,
            val comment: String,
            val submitTime: Long? = 0,
            val replyComment: String? = null,
            val replySubmitTime: Long? = 0,
            val likeCount: Int? = 0,
            val appVersion: String? = null,
            val url: String? = null
    )
}
package com.appvox.appvox.domain.response.review

data class ReviewResponse(
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
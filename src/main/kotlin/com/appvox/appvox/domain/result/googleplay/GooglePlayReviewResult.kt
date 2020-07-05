package com.appvox.appvox.domain.result.googleplay

data class GooglePlayReviewResult(
        val reviewId: String,
        val userName: String,
        val userProfilePicUrl: String,
        val rating: Int,
        val comment: String,
        val submitTime: Long,
        val likeCount: Int,
        val appVersion: String?,
        val reviewUrl: String,
        val replyComment: String?,
        val replySubmitTime: Long?
)
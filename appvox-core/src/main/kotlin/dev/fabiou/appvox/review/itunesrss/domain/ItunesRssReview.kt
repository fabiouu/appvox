package dev.fabiou.appvox.review.itunesrss.domain

import java.time.ZonedDateTime

data class ItunesRssReview(
    val id: String,
    val userName: String,
    val rating: Int,
    val title: String? = null,
    val appVersion: String? = null,
    val comment: String,
    val translatedComment: String? = null,
    val commentTime: ZonedDateTime? = null,
    val replyComment: String? = null,
    val replyTime: ZonedDateTime? = null,
    val likeCount: Int? = 0,
    val url: String? = null
)

package dev.fabiou.appvox.review.appstore.domain

import java.time.ZonedDateTime

data class AppStoreReview(

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

    val url: String? = null
)

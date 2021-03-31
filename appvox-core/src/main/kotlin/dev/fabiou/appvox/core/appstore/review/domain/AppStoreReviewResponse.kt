package dev.fabiou.appvox.core.appstore.review.domain

import java.time.ZonedDateTime

data class AppStoreReviewResponse(
    val id: String,

    val userName: String,

    val rating: Int,

    val title: String? = null,

    val appVersion: String? = null,

    val comment: String,

    var translatedComment: String? = null,

    val commentTime: ZonedDateTime? = null,

    val replyComment: String? = null,

    val replyTime: ZonedDateTime? = null,

    val url: String? = null
)

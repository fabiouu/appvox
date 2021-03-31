package dev.fabiou.appvox.core.review.googleplay.domain

import java.time.ZonedDateTime

data class GooglePlayReviewResponse(
    val reviews: List<GooglePlayReview>
) {
    data class GooglePlayReview(

        val id: String,

        val userName: String,

        val userAvatar: String? = null,

        val rating: Int,

        val title: String? = null,

        val appVersion: String? = null,

        val comment: String,

        var translatedComment: String? = null,

        val commentTime: ZonedDateTime? = null,

        val replyComment: String? = null,

        val replyTime: ZonedDateTime? = null,

        val likeCount: Int? = 0,

        val url: String? = null
    )
}

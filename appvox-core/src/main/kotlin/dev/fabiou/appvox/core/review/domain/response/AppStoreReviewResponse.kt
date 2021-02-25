package dev.fabiou.appvox.core.review.domain.response

import java.time.ZonedDateTime

data class AppStoreReviewResponse(
        val reviews: List<AppStoreReview>,
        val nextToken: String?
) {
    data class AppStoreReview(
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
        val likeCount: Int? = 0,
        val url: String? = null
    )
}
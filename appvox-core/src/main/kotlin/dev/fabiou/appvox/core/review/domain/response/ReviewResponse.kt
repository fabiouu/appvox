package dev.fabiou.appvox.core.review.domain.response

import java.time.ZonedDateTime

data class ReviewResponse(
    val reviews: List<StoreReview>,
    val nextToken: String?
) {
    data class StoreReview(

        val id: String,

        val userName: String,

        /**
         * Only available in Google Play reviews
         */
        val userAvatar: String? = null,

        val rating: Int,

        val title: String? = null,

        val appVersion: String? = null,

        val comment: String,

        var translatedComment: String? = null,

        val commentTime: ZonedDateTime? = null,

        val replyComment: String? = null,

        val replyTime: ZonedDateTime? = null,

        /**
         * Only Available in Google Play and AppStore Recent reviews
         */
        val likeCount: Int? = 0,

        val url: String? = null
    )
}

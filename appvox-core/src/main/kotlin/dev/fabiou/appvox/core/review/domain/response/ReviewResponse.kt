package dev.fabiou.appvox.core.review.domain.response

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

        val commentTime: Long? = 0,

        val replyComment: String? = null,

        val replyTime: Long? = 0,

        /**
         * Only Available in Google Play and AppStore Recent reviews
         */
        val likeCount: Int? = 0,

        val url: String? = null
    )
}

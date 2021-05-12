package dev.fabiou.appvox.review.googleplay.domain

import java.time.ZonedDateTime

data class GooglePlayReview(
    /**
     * Review Id
     */
    val id: String,

    /**
     * Google Author or user name of the user who wrote the review
     */
    val userName: String,

    /**
     * Google Avatar or profile picture of the user who wrote the review
     */
    val userAvatar: String? = null,

    /**
     * Review rating from 1 (poor) to 5 (very good)
     */
    val rating: Int,

    /**
     * Title of the review written by the user (optional, can be null)
     */
    val title: String? = null,

    /**
     * List of comments writtent by the user. If the user edited his comment, comments size will be > 1
     */
    val comments: List<UserComment>,

    /**
     * Google App Version
     */
    val appVersion: String? = null,

    /**
     * Reply comment by the App's developer
     */
    val replyComment: String? = null,

    /**
     * Time the App's developer replied to the user's comment
     */
    val replyTime: ZonedDateTime? = null,

    /**
     * Number of times users found this comment useful (thumbs-up / upvote / like)
     */
    val likeCount: Int = 0,

    /**
     * Url to the user's comment
     */
    val url: String
) {
    /**
     * Most recent comment written by the user
     */
    val latestComment: String
        get() {
            return ""
        }

    data class UserComment(
        /**
         * Comment written by the user
         */
        val comment: String,

        /**
         * Translated comment in the pre-defined target language
         */
        val translatedComment: String? = null,

        /**
         * Time the user commented on Google Play
         */
        val commentTime: ZonedDateTime,
    )

}

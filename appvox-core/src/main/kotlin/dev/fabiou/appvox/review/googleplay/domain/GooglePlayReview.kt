package dev.fabiou.appvox.review.googleplay.domain

import java.time.ZonedDateTime

data class GooglePlayReview(
    /**
     * Review Id
     */
    val id: String,

    /**
     * Url to the user's comment
     */
    val url: String,

    /**
     * List of comments written by the user and developer. If the user edited his comment, comments size will be > 1
     */
    val comments: List<Comment>,
) {
    /**
     * Most recent comment. Contains the conversation between user and developer
     */
    val latestComment: Comment
        get() = comments.first()

    /**
     * Most recent comment written by the user
     */
    val latestUserComment: UserComment
        get() = comments.first().user

    /**
     * Most recent comment written by the developer of the application
     */
    val latestDeveloperComment: DeveloperComment
        get() = comments.first().developer

    data class Comment(
        /**
         * Comment written by the user
         */
        val user: UserComment,

        /**
         * Comment written by the developer
         */
        val developer: DeveloperComment
    )

    data class UserComment(
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
         * Comment written by the user
         */
        val text: String,

        /**
         * Time the user commented on Google Play for the last time
         */
        val lastUpdateTime: ZonedDateTime,

        /**
         * Google App Version
         */
        val appVersion: String? = null,

        /**
         * Number of times users found this comment useful (thumbs-up / upvote / like)
         */
        val likeCount: Int = 0,
    )

    data class DeveloperComment(
        /**
         * Reply comment by the App's developer
         */
        val text: String? = null,

        /**
         * Time the App's developer replied to the user's comment
         */
        val lastUpdateTime: ZonedDateTime? = null,
    )
}

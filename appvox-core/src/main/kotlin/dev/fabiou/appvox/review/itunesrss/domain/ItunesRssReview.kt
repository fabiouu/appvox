package dev.fabiou.appvox.review.itunesrss.domain

import java.time.ZonedDateTime

data class ItunesRssReview(
    /**
     * Review Id
     */
    val id: String,

    /**
     * Url to the user's comment
     */
    val url: String? = null,

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
        get() = comments.first().userComment

    data class Comment(
        /**
         * Comment written by the user
         */
        val userComment: UserComment,
    )

    data class UserComment(
        /**
         * iTunes Author or User Name of the user who wrote the review
         */
        val userName: String,

        /**
         * Review rating from 1 (poor) to 5 (very good)
         */
        val rating: Int,

        /**
         * Title of the review written by the user (optional, can be null)
         */
        val title: String? = null,

        /**
         * iOS App Version
         */
        val appVersion: String? = null,

        /**
         * Comment written by the user
         */
        val text: String,

        /**
         * Time the user commented on iTunes
         */
        val time: ZonedDateTime? = null,

        /**
         * Number of times users found this comment useful (thumbs-up / upvote / like)
         */
        val likeCount: Int? = 0
    )
}

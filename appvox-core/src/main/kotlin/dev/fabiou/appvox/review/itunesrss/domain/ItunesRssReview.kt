package dev.fabiou.appvox.review.itunesrss.domain

import java.time.ZonedDateTime

data class ItunesRssReview(
    /**
     * Review Id
     */
    val id: String,

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
    val comment: String,

    /**
     * Translated comment in the pre-defined target language
     */
    val translatedComment: String? = null,

    /**
     * Time the user commented on iTunes
     */
    val commentTime: ZonedDateTime? = null,

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
    val likeCount: Int? = 0,

    /**
     * Url to the user's comment
     */
    val url: String? = null
)

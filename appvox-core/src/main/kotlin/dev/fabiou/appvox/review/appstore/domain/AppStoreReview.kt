package dev.fabiou.appvox.review.appstore.domain

import java.time.ZonedDateTime

data class AppStoreReview(
    /**
     * Review Id
     */
    val id: String,

    /**
     * App Store Author or User Name of the user who wrote the review
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
     * Url to the user's comment
     */
    val url: String? = null
)

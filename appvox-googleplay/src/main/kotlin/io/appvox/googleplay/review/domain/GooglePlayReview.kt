package io.appvox.googleplay.review.domain

import io.appvox.googleplay.review.classification.GooglePlayCommentType
import io.appvox.googleplay.review.classification.GooglePlayCommentType.*
import io.appvox.googleplay.review.classification.GooglePlayUserType
import io.appvox.googleplay.review.classification.GooglePlayUserType.*
import io.appvox.googleplay.review.constant.GooglePlayLanguage
import java.time.Period.between
import java.time.ZonedDateTime
import kotlin.math.abs

data class GooglePlayReview(

    /**
     * Review Id
     */
    val id: String,

    /**
     * Google Play language
     */
    val language: GooglePlayLanguage,

    /**
     * Whether comment has been edited by user or not.
     * Activate fetchHistory to fetch comments full history
     */
    val hasEditHistory: Boolean,

    /**
     * List of comments written by the user. Size is always >= 1.
     * If the user edited his comment, userComments size will be > 1
     */
    val userComments: List<UserComment>,

    /**
     * List of comments written by the developer.
     * If the developer replied to user's comment, developerComments size will be > 1
     */
    val developerComments: List<DeveloperComment>,
) {

    /**
     * Proxy for UserComment#name
     */
    val username get() = latestUserComment.username

    /**
     * Proxy for UserComment#avatar
     */
    val avatar get() = latestUserComment.avatar

    /**
     * See UserComment#rating
     */
    val rating get() = latestUserComment.rating

    /**
     * See UserComment#title
     */
    val title get() = latestUserComment.title

    /**
     * See UserComment#text
     */
    val text get() = latestUserComment.text

    /**
     * See UserComment#updateTime
     */
    val latestUpdateTime get() = latestUserComment.latestUpdateTime

    /**
     * See UserComment#appVersion
     */
    val appVersion get() = latestUserComment.appVersion

    /**
     * See UserComment#likeCount
     */
    val likeCount: Int get() = latestUserComment.likeCount

    companion object {
        private const val LONG_REVIEW_THRESHOLD = 150

        private const val SHORT_REVIEW_THRESHOLD = 10

        private const val MIN_NEGATIVE_REVIEW_STAR = 1

        private const val MAX_NEGATIVE_REVIEW_STAR = 3 + 1

        private const val MIN_POSITIVE_REVIEW_STAR = 4

        private const val MAX_POSITIVE_REVIEW_STAR = 5 + 1

        private const val LOYAL_USER_THRESOLD = 6

        private const val POPULAR_USER_THRESOLD = 100
    }

    val userTypes: Set<GooglePlayUserType>
        get() {
            val userTypes = HashSet<GooglePlayUserType>()
            val sortedReviews = userComments.sortedByDescending { it.latestUpdateTime }

            val firstUserComment = sortedReviews.last()
            val firstCommentTime = firstUserComment.latestUpdateTime.toLocalDate()
            val firstCommentRating = firstUserComment.rating

            val lastUserComment = sortedReviews.first()
            val lastCommentTime = lastUserComment.latestUpdateTime.toLocalDate()
            val lastCommentRating = lastUserComment.rating

            if (abs(between(firstCommentTime, lastCommentTime).months) >= LOYAL_USER_THRESOLD) userTypes.add(LOYAL)
            if (firstCommentRating > lastCommentRating) userTypes.add(DISSATISFIED)
            if (firstCommentRating < lastCommentRating) userTypes.add(SATISFIED)
            if (lastCommentRating in MIN_NEGATIVE_REVIEW_STAR..MAX_NEGATIVE_REVIEW_STAR) userTypes.add(DETRACTOR)
            if (lastCommentRating in MIN_POSITIVE_REVIEW_STAR..MAX_POSITIVE_REVIEW_STAR) userTypes.add(PROMOTER)

            return userTypes
        }

    /**
     * Most recent comment written by the user
     */
    val latestUserComment get() = userComments.first()

    /**
     * Most recent comment written by the developer of the application
     */
    val latestDeveloperComment get() = developerComments.first()

    data class UserComment(
        /**
         * Google author or username of the user who wrote the review
         */
        val username: String,

        /**
         * Google avatar or profile picture url of the user who wrote the review
         */
        val avatar: String? = null,

        /**
         * Review rating from 1 (poor) to 5 (very good)
         */
        val rating: Int,

        /**
         * Title of the review written by the user (optional)
         */
        val title: String? = null,

        /**
         * Comment written by the user
         */
        val text: String,

        /**
         * Time the user commented on Google Play for the last time
         */
        val latestUpdateTime: ZonedDateTime,

        /**
         * Google App Version
         */
        val appVersion: String? = null,

        /**
         * Number of times users found this comment useful (thumbs-up / upvote / like)
         */
        val likeCount: Int = 0
    ) {
        val types: Set<GooglePlayCommentType>
            get() {
                val reviewTypes = HashSet<GooglePlayCommentType>()
                val cleanCommentText = text.filter { !it.isWhitespace() }
                if (cleanCommentText.length > LONG_REVIEW_THRESHOLD) reviewTypes.add(EXTENSIVE)
                if (cleanCommentText.length < SHORT_REVIEW_THRESHOLD) reviewTypes.add(IRRELEVANT)
                if (likeCount >= POPULAR_USER_THRESOLD) reviewTypes.add(POPULAR)
                return reviewTypes
            }
    }

    data class DeveloperComment(
        /**
         * Reply comment by the App's developer
         */
        val text: String? = null,

        /**
         * Time the App's developer replied to the user's comment
         */
        val latestUpdateTime: ZonedDateTime? = null,
    )

    data class Criteria(
        val name: String,
        val rating: Int
    )
}

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
     * Url to the user's comment
     */
    val url: String,

    /**
     * List of comments written by the user and developer. If the user edited his comment, comments size will be > 1
     */
    val comments: List<Comment>,
) {

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
            val sortedReviews = comments.sortedByDescending { it.user.text }

            val firstUserComment = sortedReviews.last().user
            val firstCommentTime = firstUserComment.lastUpdateTime.toLocalDate()
            val firstCommentRating = firstUserComment.rating

            val lastUserComment = sortedReviews.first().user
            val lastCommentTime = lastUserComment.lastUpdateTime.toLocalDate()
            val lastCommentRating = lastUserComment.rating

            if (abs(between(firstCommentTime, lastCommentTime).months) >= LOYAL_USER_THRESOLD) userTypes.add(LOYAL)
            if (firstCommentRating > lastCommentRating) userTypes.add(DISSATISFIED)
            if (firstCommentRating < lastCommentRating) userTypes.add(SATISFIED)
            if (lastCommentRating in MIN_NEGATIVE_REVIEW_STAR..MAX_NEGATIVE_REVIEW_STAR) userTypes.add(DETRACTOR)
            if (lastCommentRating in MIN_POSITIVE_REVIEW_STAR..MAX_POSITIVE_REVIEW_STAR) userTypes.add(PROMOTER)

            return userTypes
        }

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
        val name: String,

        /**
         * Google Avatar or profile picture of the user who wrote the review
         */
        val avatar: String? = null,

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
        val likeCount: Int = 0

    ) {
        val types: Set<GooglePlayCommentType>
            get() {
                val reviewTypes = HashSet<GooglePlayCommentType>()
                val cleanCommentText = text.filter { !it.isWhitespace() }
//                when {
                if (cleanCommentText.length > LONG_REVIEW_THRESHOLD) reviewTypes.add(EXTENSIVE)
                if (cleanCommentText.length < SHORT_REVIEW_THRESHOLD) reviewTypes.add(IRRELEVANT)
                if (likeCount >= POPULAR_USER_THRESOLD) reviewTypes.add(POPULAR)
//                }
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
        val lastUpdateTime: ZonedDateTime? = null,
    )
}

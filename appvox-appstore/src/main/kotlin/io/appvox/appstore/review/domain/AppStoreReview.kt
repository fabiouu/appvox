package io.appvox.appstore.review.domain

import io.appvox.appstore.review.classification.AppStoreCommentType
import io.appvox.appstore.review.classification.AppStoreCommentType.EXTENSIVE
import io.appvox.appstore.review.classification.AppStoreCommentType.IRRELEVANT
import io.appvox.appstore.review.classification.AppStoreUserType
import io.appvox.appstore.review.classification.AppStoreUserType.DETRACTOR
import io.appvox.appstore.review.classification.AppStoreUserType.PROMOTER
import io.appvox.appstore.review.constant.AppStoreRegion
import java.time.ZonedDateTime

data class AppStoreReview(
    /**
     * Review Id
     */
    val id: String,

    /**
     * AppStore region
     */
    val region: AppStoreRegion,

    /**
     * Url to the user's comment
     */
    val url: String? = null,

    /**
     * iTunes author or name of the user who wrote the review
     */
    val username: String,

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
     * Time the user commented on iTunes
     */
    val commentTime: ZonedDateTime? = null,

    /**
     * Number of times users found this comment useful (thumbs-up / upvote / like)
     */
    val likeCount: Int = 0
) {
    companion object {
        private const val LONG_REVIEW_THRESHOLD = 150

        private const val SHORT_REVIEW_THRESHOLD = 10

        private const val MIN_NEGATIVE_REVIEW_STAR = 1

        private const val MAX_NEGATIVE_REVIEW_STAR = 3 + 1

        private const val MIN_POSITIVE_REVIEW_STAR = 4

        private const val MAX_POSITIVE_REVIEW_STAR = 5 + 1

        private const val POPULAR_USER_THRESOLD = 100
    }

    val userTypes: Set<AppStoreUserType>
        get() {
            val userPersonas = HashSet<AppStoreUserType>()
            when (rating) {
                in MIN_NEGATIVE_REVIEW_STAR..MAX_NEGATIVE_REVIEW_STAR -> userPersonas.add(DETRACTOR)
                in MIN_POSITIVE_REVIEW_STAR..MAX_POSITIVE_REVIEW_STAR -> userPersonas.add(PROMOTER)
            }
            return userPersonas
        }

    val commentTypes: Set<AppStoreCommentType>
        get() {
            val reviewTypes = HashSet<AppStoreCommentType>()
            val cleanCommentText = comment.filter { !it.isWhitespace() }
            when {
                cleanCommentText.length > LONG_REVIEW_THRESHOLD -> reviewTypes.add(EXTENSIVE)
                cleanCommentText.length < SHORT_REVIEW_THRESHOLD -> reviewTypes.add(IRRELEVANT)
                likeCount >= POPULAR_USER_THRESOLD -> reviewTypes.add(AppStoreCommentType.POPULAR)
            }
            return reviewTypes
        }
}

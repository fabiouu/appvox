package dev.fabiou.appvox.review.appstore.domain

import dev.fabiou.appvox.review.appstore.classification.AppStoreCommentType
import dev.fabiou.appvox.review.appstore.classification.AppStoreUserType
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion
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
) {

    companion object {
        private const val LONG_REVIEW_THRESHOLD = 150

        private const val SHORT_REVIEW_THRESHOLD = 10

        private const val MIN_NEGATIVE_REVIEW_STAR = 1

        private const val MAX_NEGATIVE_REVIEW_STAR = 3 + 1

        private const val MIN_POSITIVE_REVIEW_STAR = 4

        private const val MAX_POSITIVE_REVIEW_STAR = 5 + 1
    }

    val userTypes: Set<AppStoreUserType>
        get() {
            val userPersonas = HashSet<AppStoreUserType>()
            when (rating) {
                in MIN_NEGATIVE_REVIEW_STAR..MAX_NEGATIVE_REVIEW_STAR -> userPersonas.add(AppStoreUserType.DETRACTOR)
                in MIN_POSITIVE_REVIEW_STAR..MAX_POSITIVE_REVIEW_STAR -> userPersonas.add(AppStoreUserType.PROMOTER)
            }
            return userPersonas
        }

    val commentTypes: Set<AppStoreCommentType>
        get() {
            val reviewTypes = HashSet<AppStoreCommentType>()
            val cleanCommentText = comment.filter { !it.isWhitespace() }
            when {
                cleanCommentText.length > LONG_REVIEW_THRESHOLD -> reviewTypes.add(AppStoreCommentType.EXTENSIVE)
                cleanCommentText.length < SHORT_REVIEW_THRESHOLD -> reviewTypes.add(AppStoreCommentType.IRRELEVANT)
            }
            return reviewTypes
        }
}

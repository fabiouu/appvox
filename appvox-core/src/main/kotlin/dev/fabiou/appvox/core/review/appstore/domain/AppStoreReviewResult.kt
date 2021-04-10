package dev.fabiou.appvox.core.review.appstore.domain

import kotlinx.serialization.Serializable

@Serializable
internal data class AppStoreReviewResult(
        val next: String?,
        val data: List<AppStoreReview>
) {
    @Serializable
    internal data class AppStoreReview(
            val id: String,
            val type: String,
            val attributes: AppStoreReviewAttributes
    ) {
        @Serializable
        internal data class AppStoreReviewAttributes(
                val isEdited: String,
                val date: String,
                val title: String,
                val rating: Int,
                val developerResponse: AppStoreDeveloperResponse? = null,
                val review: String,
                val userName: String
        ) {
            @Serializable
            internal data class AppStoreDeveloperResponse(
                    val id: Long,
                    val body: String,
                    val modified: String
            )
        }
    }
}
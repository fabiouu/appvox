package dev.fabiou.appvox.core.review.appstore.domain

import com.fasterxml.jackson.annotation.JsonProperty

internal data class AppStoreReviewResult(
    @JsonProperty("next") val next: String?,
    @JsonProperty("data") val data: List<AppStoreReview>
) {

    internal data class AppStoreReview(
        @JsonProperty("id") val id: String,
        @JsonProperty("type") val type: String,
        @JsonProperty("attributes") val attributes: AppStoreReviewAttributes
    ) {
        internal data class AppStoreReviewAttributes(
            @JsonProperty("isEdited") val isEdited: String,
            @JsonProperty("date") val date: String,
            @JsonProperty("title") val title: String,
            @JsonProperty("rating") val rating: Int,
            @JsonProperty("developerResponse") val developerResponse: AppStoreDeveloperResponse?,
            @JsonProperty("review") val review: String,
            @JsonProperty("userName") val userName: String
        ) {
            internal data class AppStoreDeveloperResponse(
                @JsonProperty("id") val id: Long,
                @JsonProperty("body") val body: String,
                @JsonProperty("modified") val modified: String
            )
        }
    }
}

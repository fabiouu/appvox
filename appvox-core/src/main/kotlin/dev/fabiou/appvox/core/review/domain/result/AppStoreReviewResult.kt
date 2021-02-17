package dev.fabiou.appvox.core.review.domain.result

import com.fasterxml.jackson.annotation.JsonProperty

data class AppStoreReviewResult(
    @JsonProperty("next") val next: String?,
    @JsonProperty("data") val data: List<AppStoreReview>
) {

    data class AppStoreReview(
        @JsonProperty("id") val id: String,
        @JsonProperty("type") val type: String,
        @JsonProperty("attributes") val attributes: AppStoreReviewAttributes
    ) {
        data class AppStoreReviewAttributes(
            @JsonProperty("isEdited") val isEdited : String,
            @JsonProperty("date") val date : String,
            @JsonProperty("title") val title : String,
            @JsonProperty("rating") val rating : Int,
            @JsonProperty("developerResponse") val developerResponse: AppStoreDeveloperResponse?,
            @JsonProperty("review") val review : String,
            @JsonProperty("userName") val userName : String
        ) {
            data class AppStoreDeveloperResponse(
                @JsonProperty("id") val id : Long,
                @JsonProperty("body") val body : String,
                @JsonProperty("modified") val modified : String
            )
        }
    }
}
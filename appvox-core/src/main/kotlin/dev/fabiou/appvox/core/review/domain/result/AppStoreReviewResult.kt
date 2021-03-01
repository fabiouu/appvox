package dev.fabiou.appvox.core.review.domain.result

import com.fasterxml.jackson.annotation.JsonProperty
import dev.fabiou.appvox.core.review.domain.response.ReviewResponse
import java.time.ZonedDateTime

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

    fun toResponse() : ReviewResponse {
        var reviews = ArrayList<ReviewResponse.StoreReview>()
        val appStoreReviews = this.data
        for (appStoreReview in appStoreReviews) {
            val reviewResponse = ReviewResponse.StoreReview(
                id = appStoreReview.id,
                userName = appStoreReview.attributes.userName,
                rating = appStoreReview.attributes.rating,
                title = appStoreReview.attributes.title,
                comment = appStoreReview.attributes.review,
                commentTime = ZonedDateTime.parse(appStoreReview.attributes.date),
                replyComment = appStoreReview.attributes.developerResponse?.body,
                replyTime = ZonedDateTime.parse(appStoreReview.attributes.developerResponse?.modified),
//                        url =
            )
            reviews.add(reviewResponse)
        }

        return ReviewResponse(
            reviews = reviews,
            nextToken = this.next
        )
    }
}
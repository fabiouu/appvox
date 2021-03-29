package dev.fabiou.appvox.core.appstore.review.domain

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

//    fun toResponse() : AppReviewResponse {
//        var reviews = ArrayList<AppReviewResponse.AppReview>()
//        val appStoreReviews = this.data
//        for (appStoreReview in appStoreReviews) {
//            val reviewResponse = AppReviewResponse.AppReview(
//                id = appStoreReview.id,
//                userName = appStoreReview.attributes.userName,
//                rating = appStoreReview.attributes.rating,
//                title = appStoreReview.attributes.title,
//                comment = appStoreReview.attributes.review,
//                commentTime = ZonedDateTime.parse(appStoreReview.attributes.date),
//                replyComment = appStoreReview.attributes.developerResponse?.body,
//                replyTime = ZonedDateTime.parse(appStoreReview.attributes.developerResponse?.modified),
////                        url =
//            )
//            reviews.add(reviewResponse)
//        }
//
//        return AppReviewResponse(
//            reviews = reviews,
//            nextToken = this.next
//        )
//    }
}
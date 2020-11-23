package com.appvox.core.review.domain.result

data class AppStoreReviewResult(
        val next: String?,
        val data: List<AppStoreReview>
) {
    data class AppStoreReview(
            val id: String,
            val type: String,
            val attributes: AppStoreReviewAttributes
    ) {
        data class AppStoreReviewAttributes(
                val isEdited : String,
                val date : String,
                val title : String,
                val rating : Int,
                val developerResponse: AppStoreDeveloperResponse?,
                val review : String,
                val userName : String
        ) {
            data class AppStoreDeveloperResponse(
                    val id : Long,
                    val body : String,
                    val modified : String
            )
        }
    }
}
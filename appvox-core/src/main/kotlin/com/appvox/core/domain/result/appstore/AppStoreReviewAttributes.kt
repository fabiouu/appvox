package com.appvox.core.domain.result.appstore

data class AppStoreReviewAttributes(
        val isEdited : String,
        val date : String,
        val title : String,
        val rating : Int,
        val developerResponse: AppStoreDeveloperResponse?,
        val review : String,
        val userName : String
)
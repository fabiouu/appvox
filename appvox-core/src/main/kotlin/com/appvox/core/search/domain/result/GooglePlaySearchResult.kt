package com.appvox.core.search.domain.result

data class GooglePlaySearchResult(
    val appId : String? = "",
    val appName : String? = "",
    val appDescription : String? = "",
    val appRating : String? = "",
    val appRatingDetails : String? = "",
    val appPic : String? = "",
    val appDeveloperName : String? = "",
    val appDeveloperLink : String? = "",
    val appLink : String? = ""
)
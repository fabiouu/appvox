package com.appvox.core.review.domain.request

class AppStoreReviewRequest(
         val region: String,
         var bearerToken: String? = null,
         val fetchCountLimit: Int = 0,
         var nextToken: String? = null
)
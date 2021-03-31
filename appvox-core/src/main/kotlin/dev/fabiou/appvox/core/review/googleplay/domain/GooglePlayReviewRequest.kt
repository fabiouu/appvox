package dev.fabiou.appvox.core.review.googleplay.domain

import dev.fabiou.appvox.core.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.review.googleplay.constant.GooglePlaySortType

 class GooglePlayReviewRequest(
         val appId: String,
         val maxCount: Int,// = Int.MAX_VALUE,
         var nextToken: String? = null,
         val language: GooglePlayLanguage,// = GooglePlayLanguage.ENGLISH_US,
         val sortType: GooglePlaySortType,// = GooglePlaySortType.RECENT,
         val batchSize: Int,// = 100,
//    appId: String,
//    maxCount: Int = Int.MAX_VALUE,
//    nextToken: String? = null
)// : BaseReviewRequest(appId, maxCount, nextToken)
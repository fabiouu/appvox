package dev.fabiou.appvox.core.appstore.review

internal class ItunesRssReviewRequest(
    val appId: String,
    val maxCount: Int = Int.MAX_VALUE,
    var nextToken: String? = null,
    val region: String,
    val sortType: AppStoreSortType = AppStoreSortType.RECENT,
    var pageNo: Int = 1
//    appId: String,
//    maxCount: Int = Int.MAX_VALUE,
//    nextToken: String? = null
)// : BaseReviewRequest(appId, maxCount, nextToken)
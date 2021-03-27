package dev.fabiou.appvox.core.appstore.review

internal class AppStoreReviewRequest(
    val appId: String,
    val maxCount: Int = Int.MAX_VALUE,
    var nextToken: String? = null,
    val region: String,
    val sortType: AppStoreSortType = AppStoreSortType.RECENT,
    var bearerToken: String? = null
//    appId: String,
//    maxCount: Int = Int.MAX_VALUE,
//    nextToken: String? = null
)// : BaseReviewRequest(appId, maxCount, nextToken) {
//    fun fromAppReviewRequest(request : AppReviewRequest) {
//        return AppStoreReviewRequest(
//            appId = request.appId,
//            region = request.region,
//            sortType = request.sortType,
//            pageNo = 1,
//            maxCount request., bearerToken, nextToken
//        )
//    }
//}
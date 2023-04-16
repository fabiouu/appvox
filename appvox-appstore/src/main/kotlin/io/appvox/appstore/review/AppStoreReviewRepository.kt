//package io.appvox.appstore.review
//
//import io.appvox.appstore.review.domain.AppStoreReviewEntry
//import io.appvox.appstore.review.domain.AppStoreReviewJsonResultTop
//import io.appvox.appstore.review.domain.AppStoreReviewRequestParameters
//import io.appvox.core.configuration.RequestConfiguration
//import io.appvox.core.exception.AppVoxError
//import io.appvox.core.exception.AppVoxException
//import io.appvox.core.review.ReviewRequest
//import io.appvox.core.review.ReviewResult
//import io.appvox.core.util.HttpUtil
//import kotlinx.serialization.decodeFromString
//import kotlinx.serialization.json.Json
//import java.io.IOException
//
//val json = Json { isLenient = true; ignoreUnknownKeys = true }
//
//internal class AppStoreReviewRepository(
//    private val config: RequestConfiguration
//) {
//    companion object {
//        internal var REQUEST_URL_DOMAIN = "https://itunes.apple.com"
//        internal const val REQUEST_URL_PATH = "/%s/rss/customerreviews/page=%d/id=%s/sortby=%s/json"
//        internal const val REQUEST_URL_PARAMS = "?urlDesc=/customerreviews/id=%s/%s/json"
//        internal const val MIN_PAGE_NO = 1
//        internal const val MAX_PAGE_NO = 10
//    }
//
//    private val httpUtil = HttpUtil
//
//    @Throws(AppVoxException::class)
//    fun getReviewsByAppId(
//        request: ReviewRequest<AppStoreReviewRequestParameters>
//    ): ReviewResult<AppStoreReviewEntry> {
//        val result : AppStoreReviewJsonResultTop = try {
//            if (request.parameters.pageNo !in MIN_PAGE_NO..MAX_PAGE_NO) {
//                throw AppVoxException(AppVoxError.INVALID_ARGUMENT)
//            }
//            val requestUrl = request.nextToken ?: (REQUEST_URL_DOMAIN +
//                REQUEST_URL_PATH.format(
//                    request.parameters.region.code,
//                    request.parameters.pageNo,
//                    request.parameters.appId,
//                    request.parameters.sortType.value
//                ) +
//                REQUEST_URL_PARAMS.format(
//                    request.parameters.appId,
//                    request.parameters.sortType.value
//                ))
//                val responseBody = httpUtil.getRequest(requestUrl = requestUrl, proxy = config.proxy)
//                json.decodeFromString(responseBody)
//        } catch (e: IOException) {
//            throw AppVoxException(AppVoxError.NETWORK, e)
//        }
//        return ReviewResult(
//            results = result.feed.entry,
////            nextToken = result.link?.find { it.rel == "next" }?.href
//        )
//    }
//}

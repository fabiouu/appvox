//package io.appvox.appstore
//
//import io.appvox.core.configuration.RequestConfiguration
//import io.appvox.core.util.HttpUtil
//
///**
// * This class consists of the main methods for interacting with iTunes RSS feed
// */
//class AppStore(
//    private val config: RequestConfiguration = RequestConfiguration()
//) {
////    private val appStoreReviewService = AppStoreReviewService(config)
//
//    init {
//        config.proxyAuthentication?.let { it ->
//            HttpUtil.setAuthenticator(it.userName, it.password)
//        }
//    }
//
//    /**
//     * Returns a flow of Reviews from iTunes RSS Feed
//     */
////    fun reviews(parameters: AppStoreReviewRequestParameters.Builder.() -> Unit): Flow<AppStoreReview> {
////        if (config.delay < MIN_REQUEST_DELAY) {
////            throw AppVoxException(AppVoxError.REQ_DELAY_TOO_SHORT)
////        }
////        val appStoreRequest = AppStoreReviewRequestParameters.Builder().apply(parameters).build()
////        val initialRequest = ReviewRequest(appStoreRequest)
////        return appStoreReviewService.getReviewsByAppId(initialRequest)
////    }
//}

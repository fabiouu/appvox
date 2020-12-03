//import com.appvox.core.configuration.Configuration
//import com.appvox.core.configuration.ProxyConfiguration
//import com.appvox.core.review.domain.request.AppStoreReviewRequest
//import com.appvox.core.review.facade.AppStoreReviewFacade
//
//
//fun main(args: Array<String>) {
//    val request = AppStoreReviewRequest(region = "us", size = 10, nextToken = "", bearerToken = "")
////    getAppStoreReviews(appId ="com.twitter.android", request = request)
//    getAppStoreReviewsThroughProxy(appId ="333903271", request = request)
//}
//
//fun getAppStoreReviews(appId : String, request : AppStoreReviewRequest) {
//    val service = AppStoreReviewFacade()
//    val response = service.getReviewsByAppId(appId, request)
//
//    println(response.toString())
//}
//
//fun getAppStoreReviewsThroughProxy(appId : String, request : AppStoreReviewRequest) {
////    val configuration = Configuration.Builder()
////            .proxy(ProxyConfiguration.Builder()
////            .host("127.0.0.1")
////            .port(1080)
////            .build()).build();
////    val service = AppStoreReviewFacade(configuration)
////    val response = service.getReviewsByAppId(appId, request)
//
////    println(response.reviews.first().comment)
//}
//// Java/Kotlin Iterators interoperael iteartors
//// Interoable Ietrators must be implemented from the basis of a Java operator
//// And it should be compatible with Java based softwRE
//fun getAppStoreReviewsThroughProxyWithIterator(appId : String, request : AppStoreReviewRequest) {
//    val configuration = Configuration.Builder()
//            .proxy(ProxyConfiguration.Builder()
//                    .host("127.0.0.1")
//                    .port(1090)
//                    .build()).build();
//    val service = AppStoreReviewFacade(configuration)
//    val response = service.getReviewsByAppId(appId, request)
//
//    //service.getReviewsByAppId(appId, request)
////    while () {
////
////    }
//
//    println(response.reviews.first().comment)
//}
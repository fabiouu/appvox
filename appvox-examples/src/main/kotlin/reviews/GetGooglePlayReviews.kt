package reviews//import com.appvox.core.configuration.Configuration
//import com.appvox.core.configuration.ProxyConfiguration
//import com.appvox.core.review.domain.request.GooglePlayReviewRequest
//import com.appvox.core.review.facade.GooglePlayReviewFacade
//
//
//fun main(args: Array<String>) {
//    val request = GooglePlayReviewRequest(language = "en", sortType = 1, size = 40)
//    getGooglePlayReviews(appId ="com.twitter.android", request = request)
//    getGooglePlayReviewsThroughProxy(appId ="com.twitter.android", request = request)
//}
//
//fun getGooglePlayReviews(appId : String, request : GooglePlayReviewRequest) {
//    val service = GooglePlayReviewFacade()
//    val response = service.getReviewsByAppId(appId, request)
//
//    println(response.toString())
//}
//
//fun getGooglePlayReviewsThroughProxy(appId : String, request : GooglePlayReviewRequest) {
//    val configuration = Configuration.Builder().proxy(
//        ProxyConfiguration.Builder()
//            .host("127.0.0.1")
//            .port(1087)
//            .build()
//    ).build();
//    val service = GooglePlayReviewFacade(configuration)
//    val response = service.getReviewsByAppId(appId, request)
//
//    println(response.toString())
//}
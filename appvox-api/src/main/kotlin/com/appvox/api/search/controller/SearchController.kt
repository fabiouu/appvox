//package com.appvox.api.search.controller
//
//import com.appvox.core.review.domain.request.AppStoreReviewRequest
//import com.appvox.core.review.domain.request.GooglePlayReviewRequest
//import com.appvox.core.review.domain.response.ReviewsResponse
//import com.appvox.core.review.facade.AppStoreReviewFacade
//import com.appvox.core.review.facade.GooglePlayReviewFacade
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.PathVariable
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//class SearchController {
//
//    @GetMapping("/store/google-play/app/{appId}/reviews")
//    fun getReviewsByAppId(
//            @PathVariable(value = "appId") appId: String,
//            request : GooglePlayReviewRequest) : ReviewsResponse {
//        val facade = GooglePlayReviewFacade()
////        val reviews = facade.getReviewsByAppId(appId, request)
//        return reviews
//    }
//
//    @GetMapping("/store/app-store/app/{appId}/reviews")
//    fun getReviewsByAppId(
//            @PathVariable(value = "appId") appId : String,
//            request : AppStoreReviewRequest) : ReviewsResponse {
//        val facade = AppStoreReviewFacade()
//        val reviews = facade.getReviewsByAppId(appId, request)
//        return reviews
//    }
//}
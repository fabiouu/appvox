//package io.appvox.appstore.review
//
//import io.appvox.appstore.review.domain.AppStoreReview
//import io.appvox.appstore.review.domain.AppStoreReviewRequestParameters
//
//internal class AppStoreReviewConverter {
//    fun toResponse(
//        requestParameters: AppStoreReviewRequestParameters,
//        result: AppStoreReviewResult.Entry
//    ): AppStoreReview {
//        return AppStoreReview(
//            id = result.id!!,
//            region = requestParameters.region,
//            url = result.link?.href,
//            username = result.author?.name!!,
//            rating = result.rating,
//            title = result.title,
//            comment = result.content?.find { it.type == "text" }?.text!!,
//            commentTime = result.updated?.toGregorianCalendar()?.toZonedDateTime(),
//            appVersion = result.version,
//            likeCount = result.voteCount,
//        )
//    }
//}

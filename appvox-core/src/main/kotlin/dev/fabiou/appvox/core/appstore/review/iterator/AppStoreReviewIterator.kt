//package dev.fabiou.appvox.core.appstore.review.iterator
//
//import dev.fabiou.appvox.core.appstore.review.converter.AppStoreReviewConverter
//import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewRequest
//import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewResponse
//import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewResult
//import dev.fabiou.appvox.core.appstore.review.service.AppStoreReviewService
//import dev.fabiou.appvox.core.exception.AppVoxErrorCode
//import dev.fabiou.appvox.core.exception.AppVoxException
//
//class AppStoreReviewIterator(
//        private val service: AppStoreReviewService,
//        private val request: AppStoreReviewRequest
//) : Iterator<AppStoreReviewResponse> {
//
//    var currentIndex: Int = 0
//
//    var iterator: Iterator<AppStoreReviewResult.AppStoreReview>
//
//    init {
//        val response = service.getReviewsByAppId(request)
//        iterator = response.result.data.iterator()
//    }
//
//    override fun hasNext(): Boolean {
//
//        if (service.config.requestDelay < 500) {
//            throw AppVoxException(AppVoxErrorCode.REQ_DELAY_TOO_SHORT)
//        }
//
//        if (request.maxCount != 0 && currentIndex == request.maxCount) {
//            return false
//        }
//
//        if (request.nextToken == null && !iterator.hasNext()) {
//            return false
//        }
//
//        if (!iterator.hasNext()) {
//            Thread.sleep(service.config.requestDelay)
//            val response = service.getReviewsByAppId(request)
//            if (response.result.data.isEmpty()) {
//                return false
//            }
//            iterator = response.result.data.iterator()
//            request.nextToken = response.nextToken
//        }
//
//        return true
//    }
//
//    override fun next(): AppStoreReviewResponse {
//        currentIndex++
//        return AppStoreReviewConverter.toResponse(iterator.next())
//    }
//}
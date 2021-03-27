package dev.fabiou.appvox.core.googleplay.review

import dev.fabiou.appvox.core.exception.AppVoxErrorCode
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.archive.AppReviewRequest
import dev.fabiou.appvox.core.archive.AppReviewResponse.AppReview
import dev.fabiou.appvox.core.archive.AppReviewService

//class AppReviewIterable(
//    private val service: AppReviewService,
//    private val request: AppReviewRequest
//) : Iterable<AppReview> {
//
//    @Throws(AppVoxException::class)
//    override fun iterator(): Iterator<AppReview> {
//        return AppReviewIterator(service, request)
//    }

class GooglePlayReviewIterator(
    private val service: AppReviewService,
    private val request: AppReviewRequest
) : Iterator<AppReview> {

    var currentIndex: Int = 0

    var iterator: Iterator<AppReview>

    var nextToken: String? = null

    init {
        val response = service.getReviewsByAppId(request)
        iterator = response.reviews.iterator()
        nextToken = response.nextToken
//            request.nextToken = response.nextToken
    }

    override fun hasNext(): Boolean {

        if (service.config.requestDelay < 500) {
            throw AppVoxException(AppVoxErrorCode.REQ_DELAY_TOO_SHORT)
        }

        if (request.maxCount != 0 && currentIndex == request.maxCount) {
            return false
        }

//            if (request.nextToken == null && !iterator.hasNext()) {
//                return false
//            }

        if (!iterator.hasNext()) {
            Thread.sleep(service.config.requestDelay)
            val response = service.getReviewsByAppId(request)
            if (response.reviews.isEmpty()) {
                return false
            }
            iterator = response.reviews.iterator()
//            request.nextToken = response.nextToken
        }

        return true
    }

    override fun next(): AppReview {
        currentIndex++
        return iterator.next()
    }
}

//        fun getReviewsTest(request: AppReviewRequest): AppReviewResponse {
//            val appId = request.appId.toIntOrNull()
//            // If appid is a number then its an AppStore App (not really actually but for now only support digit appId)
//            if (appId != null) {
//                if (request.sortType == AppReviewSortType.RELEVANT) {
////                        AppStoreReviewRequest
//                    service.getReviewsByAppId(request)
//                }
//            } else {
//
//            }
//
//        }
//    @Throws(AppVoxException::class)
//    override fun iterator(): Iterator<AppReview> {
//        return object : Iterator<AppReview> {
//
//            var currentIndex : Int = 0
//
//            var iterator: Iterator<AppReview>
//
//            init {
//                val response = service.getReviewsByAppId(request)
//                iterator = response.reviews.iterator()
//                request.nextToken = response.nextToken
//            }
//
//            override fun hasNext(): Boolean {
//
//                if (service.config.requestDelay < 500) {
//                    throw AppVoxException(AppVoxErrorCode.REQ_DELAY_TOO_SHORT)
//                }
//
//                if (request.maxCount != 0 && currentIndex == request.maxCount) {
//                    return false
//                }
//
//                if (request.nextToken == null && !iterator.hasNext()) {
//                    return false
//                }
//
//                if (!iterator.hasNext()) {
//                    Thread.sleep(service.config.requestDelay)
//                    val response = service.getReviewsByAppId(request)
//                    if (response.reviews.isEmpty()) {
//                        return false
//                    }
//                    iterator = response.reviews.iterator()
//                    request.nextToken = response.nextToken
//                }
//
//                return true
//            }
//
//            fun getReviewsTest(request : AppReviewRequest) : AppReviewResponse {
//                val appId = request.appId.toIntOrNull()
//                // If appid is a number then its an AppStore App (not really actually but for now only support digit appId)
//                if (appId != null) {
//                    if (request.sortType == AppReviewSortType.RELEVANT) {
////                        AppStoreReviewRequest
//                        service.getReviewsByAppId(request)
//                    }
//                } else {
//
//                }
//
//            }
//
//            override fun next(): AppReview {
//                currentIndex++
//                return iterator.next()
//            }
//        }
//    }
//}

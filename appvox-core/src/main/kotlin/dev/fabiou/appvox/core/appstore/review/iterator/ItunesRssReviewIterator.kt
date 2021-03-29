//package dev.fabiou.appvox.core.appstore.review.iterator
//
//import dev.fabiou.appvox.core.appstore.review.domain.AppStoreReviewResult
//import dev.fabiou.appvox.core.appstore.review.domain.ItunesRssReviewRequest
//import dev.fabiou.appvox.core.appstore.review.domain.ItunesRssReviewResponse
//import dev.fabiou.appvox.core.appstore.review.domain.ItunesRssReviewResult
//import dev.fabiou.appvox.core.appstore.review.service.ItunesRssReviewService
//import dev.fabiou.appvox.core.exception.AppVoxErrorCode
//import dev.fabiou.appvox.core.exception.AppVoxException
//
//class ItunesRssReviewIterator(
//        private val service: ItunesRssReviewService,
//        private val request: ItunesRssReviewRequest
//) : Iterator<ItunesRssReviewResponse> {
//
//    var currentIndex: Int = 0
//
//    var iterator: Iterator<ItunesRssReviewResult.Entry>
//
//    init {
//        val response = service.getReviewsByAppId(request)
//        iterator = response.entry!!.iterator()
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
//            if (response.entry!!.isEmpty()) {
//                return false
//            }
//            iterator = response.entry!!.iterator()
////            request.nextToken = response.n
////            this.link!!.find { it.rel == "next" }?.href!!
//        }
//
//        return true
//    }
//
//    override fun next(): ItunesRssReviewResponse {
//        currentIndex++
//        // Conversion to response
////        return iterator.next()
//    }
//}
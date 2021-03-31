package dev.fabiou.appvox.core.review.itunesrss

import dev.fabiou.appvox.core.exception.AppVoxErrorCode
import dev.fabiou.appvox.core.exception.AppVoxException
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewRequest
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewResponse
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewResult

// TODO: Modify Iterator to return list of reviews for each network call and move delay mecanism outside = decoupling
internal class ItunesRssReviewIterator(
        private val service: ItunesRssReviewService,
        private val request: ItunesRssReviewRequest
) : Iterator<ItunesRssReviewResponse> {

    var currentIndex: Int = 0

    var iterator: Iterator<ItunesRssReviewResult.Entry>

    init {
        val response = service.getReviewsByAppId(request)
        iterator = response.result.entry!!.iterator()
        request.nextToken = response.nextToken
    }

    override fun hasNext(): Boolean {

        if (service.config.requestDelay < 500) {
            throw AppVoxException(AppVoxErrorCode.REQ_DELAY_TOO_SHORT)
        }

        if (request.maxCount != 0 && currentIndex == request.maxCount) {
            return false
        }

        if (request.nextToken == null && !iterator.hasNext()) {
            return false
        }

        if (!iterator.hasNext()) {
//            Thread.sleep(service.config.requestDelay)
//            delay(100)
            val response = service.getReviewsByAppId(request)
            if (response.result.entry!!.isEmpty()) {
                return false
            }
            iterator = response.result.entry!!.iterator()
            request.nextToken = response.nextToken
        }

        return true
    }

    override fun next(): ItunesRssReviewResponse {
        currentIndex++
        return ItunesRssReviewConverter.toResponse(iterator.next())
    }
}
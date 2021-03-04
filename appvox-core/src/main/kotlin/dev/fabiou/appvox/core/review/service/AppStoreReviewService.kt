package dev.fabiou.appvox.core.review.service

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.constant.AppReviewSortType
import dev.fabiou.appvox.core.review.constant.AppStoreSortType
import dev.fabiou.appvox.core.review.domain.request.AppStoreReviewRequest
import dev.fabiou.appvox.core.review.domain.request.ReviewRequest
import dev.fabiou.appvox.core.review.domain.response.ReviewResponse
import dev.fabiou.appvox.core.review.repository.ItunesRssReviewRepository
import dev.fabiou.appvox.core.review.repository.AppStoreReviewRepository
import dev.fabiou.appvox.core.translation.TranslationService

class AppStoreReviewService(
        val config: Configuration
) {
    private lateinit var translationService : TranslationService

    private var recentReviewRepository = ItunesRssReviewRepository(config)

    private var reviewRepository = AppStoreReviewRepository(config)

    init {
        try {
            val cls = Class.forName("dev.fabiou.appvox.core.translation.GoogleTranslationImpl")
            translationService = cls.newInstance() as TranslationService
        } catch (e: Exception) {

        }
    }

    fun getReviewsByAppId(request: ReviewRequest, nextToken: String, bearerToken: String) : ReviewResponse {
        var response : ReviewResponse
        if (request.sortType == AppReviewSortType.RECENT) {
            var reviews = recentReviewRepository.getReviewsByAppId(request)
            response = reviews.toResponse()
        } else {
            val bearerToken = request.bearerToken ?: reviewRepository.getBearerToken(request.appId, request.region)
            request.bearerToken = bearerToken
            val reviews = reviewRepository.getReviewsByAppId(request, bearerToken)
            response = reviews.toResponse()
        }

        return response
    }
}
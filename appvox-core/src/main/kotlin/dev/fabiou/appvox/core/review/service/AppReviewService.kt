package dev.fabiou.appvox.core.review.service

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.domain.request.AppStoreReviewRequest
import dev.fabiou.appvox.core.review.domain.request.GooglePlayReviewRequest
import dev.fabiou.appvox.core.review.domain.request.ItunesRssReviewRequest
import dev.fabiou.appvox.core.review.domain.result.AppStoreRecentReviewResult
import dev.fabiou.appvox.core.review.domain.result.AppStoreReviewResult
import dev.fabiou.appvox.core.review.domain.result.GooglePlayReviewResult
import dev.fabiou.appvox.core.review.repository.AppStoreReviewRepository
import dev.fabiou.appvox.core.review.repository.GooglePlayReviewRepository
import dev.fabiou.appvox.core.review.repository.ItunesRssReviewRepository

class AppReviewService(
    val config : Configuration = Configuration()
) {
    private var googlePlayReviewRepository = GooglePlayReviewRepository(config)

    private var itunesRssReviewRepository = ItunesRssReviewRepository(config)

    private var appStoreReviewRepository = AppStoreReviewRepository(config)

    fun getReviewsByAppId(request: ItunesRssReviewRequest) : AppStoreRecentReviewResult {
        var reviews = itunesRssReviewRepository.getReviewsByAppId(request)
        return reviews
    }

    fun getReviewByAppId(request: AppStoreReviewRequest) : AppStoreReviewResult {
        request.bearerToken = request.bearerToken ?: appStoreReviewRepository.getBearerToken(request.appId, request.region)
        val result = appStoreReviewRepository.getReviewsByAppId(request)
        return result
    }

    fun getReviewsByAppId(request: GooglePlayReviewRequest) : GooglePlayReviewResult {
        val result = googlePlayReviewRepository.getReviewsByAppId(request)
        return result
    }
}
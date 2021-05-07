package dev.fabiou.appvox.review.googleplay

import dev.fabiou.appvox.app.googleplay.GooglePlayRepository
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.ReviewResult
import dev.fabiou.appvox.review.ReviewService
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewRequest
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewResult

internal class GooglePlayReviewService(
    val config: RequestConfiguration
) : ReviewService<GooglePlayReviewRequest, GooglePlayReviewResult> {

    private val googlePlayReviewRepository = GooglePlayReviewRepository(config)

    private val googlePlayRepository = GooglePlayRepository(config)

//    init {
//        val scriptParameters = googlePlayRepository.getScriptParameters(
//            request.parameters.appId,
//            request.parameters.language
//        )
//    }

    override fun getReviewsByAppId(
        request: ReviewRequest<GooglePlayReviewRequest>
    ): ReviewResult<GooglePlayReviewResult> {

//        val scriptParameters = googlePlayRepository.getScriptParameters(
//            request.parameters.appId,
//            request.parameters.language
//        )

        return googlePlayReviewRepository.getReviewsByAppId(request)
    }
}

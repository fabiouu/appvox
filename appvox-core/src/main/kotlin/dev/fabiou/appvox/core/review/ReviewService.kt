package dev.fabiou.appvox.core.review

internal interface ReviewService<Request, Result> {
    fun getReviewsByAppId(request: ReviewRequest<Request>): ReviewResult<Result>
}
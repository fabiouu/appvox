package dev.fabiou.appvox.review

internal interface ReviewService<Request, Result> {
    fun getReviewsByAppId(request: ReviewRequest<Request>): ReviewResult<Result>
}

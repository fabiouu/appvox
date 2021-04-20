package dev.fabiou.appvox.review

internal interface ReviewRepository<Request, Result> {
    fun getReviewsByAppId(request: ReviewRequest<Request>): ReviewResult<Result>
}

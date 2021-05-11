package dev.fabiou.appvox.review

internal interface ReviewService<Request, Result> {
    suspend fun getReviewsByAppId(request: ReviewRequest<Request>): ReviewResult<Result>
}

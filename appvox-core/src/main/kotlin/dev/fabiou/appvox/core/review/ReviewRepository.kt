package dev.fabiou.appvox.core.review

internal interface ReviewRepository<Request, Result> {
    fun getReviewsByAppId(request: Request): ReviewResult<Result>
}
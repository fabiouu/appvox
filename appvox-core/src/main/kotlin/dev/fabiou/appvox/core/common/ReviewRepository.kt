package dev.fabiou.appvox.core.common

internal interface ReviewRepository<Request, Result> {
    fun getReviewsByAppId(request: Request): ReviewResult<Result>
}
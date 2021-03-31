package dev.fabiou.appvox.core.review

class ReviewResult<Result>(
    val result: Result,
    val nextToken: String? = null
)
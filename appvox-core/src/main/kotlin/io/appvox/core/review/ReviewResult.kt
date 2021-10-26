package io.appvox.core.review

class ReviewResult<Result>(
    val results: List<Result>?,
    val nextToken: String? = null
)

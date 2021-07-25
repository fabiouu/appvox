package dev.fabiou.appvox.review

internal class ReviewResult<Result>(
    val results: List<Result>,
    val nextToken: String? = null
)

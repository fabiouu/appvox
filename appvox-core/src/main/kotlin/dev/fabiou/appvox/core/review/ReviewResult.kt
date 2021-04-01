package dev.fabiou.appvox.core.review

internal class ReviewResult<Result>(
    val results: List<Result>,
    val nextToken: String? = null
)
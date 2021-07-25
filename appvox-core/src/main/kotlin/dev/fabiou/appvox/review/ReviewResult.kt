package dev.fabiou.appvox.review

class ReviewResult<Result>(
    val results: List<Result>,
    val nextToken: String? = null
)

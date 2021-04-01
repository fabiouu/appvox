package dev.fabiou.appvox.core.review

internal data class ReviewRequest<RequestParameters>(
    val parameters: RequestParameters,
    var nextToken: String? = null
)
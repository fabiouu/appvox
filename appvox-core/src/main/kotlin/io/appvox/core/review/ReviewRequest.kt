package io.appvox.core.review

data class ReviewRequest<RequestParameters>(
    val parameters: RequestParameters,
    val nextToken: String? = null
)

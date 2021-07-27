package io.appvox.review

data class ReviewRequest<RequestParameters>(
    val parameters: RequestParameters,
    val nextToken: String? = null
)

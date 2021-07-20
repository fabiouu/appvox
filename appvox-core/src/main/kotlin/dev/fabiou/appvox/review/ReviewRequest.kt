package dev.fabiou.appvox.review

@PublishedApi internal data class ReviewRequest<RequestParameters>(
    val parameters: RequestParameters,
    val nextToken: String? = null
)

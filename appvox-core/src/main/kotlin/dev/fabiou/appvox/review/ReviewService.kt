package dev.fabiou.appvox.review

import kotlinx.coroutines.flow.Flow

internal interface ReviewService<Request, Result, Response> {
    fun getReviewsByAppId(initialRequest: ReviewRequest<Request>): Flow<Response>
}

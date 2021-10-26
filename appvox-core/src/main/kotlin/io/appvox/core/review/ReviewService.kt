package io.appvox.core.review

import kotlinx.coroutines.flow.Flow

interface ReviewService<Request, Result, Response> {
    fun getReviewsByAppId(initialRequest: ReviewRequest<Request>): Flow<Response>
}

package dev.fabiou.appvox.core.review

internal class ReviewIterator<Request, Result, Response>(
    private val service: ReviewService<Request, Result>,
    private val converter: ReviewConverter<Result, Response>,
    private var request: ReviewRequest<Request>
) : Iterator<List<Response>> {

    private val results = mutableListOf<Result>()

    override fun hasNext(): Boolean {
        val response = service.getReviewsByAppId(request)
        results.clear()
        results.addAll(response.results)
        request = request.copy(request.parameters, response.nextToken)
        return request.nextToken != null
    }

    override fun next(): List<Response> {
        if (!hasNext()) {
            throw NoSuchElementException()
        }
        return converter.toResponse(results)
    }
}

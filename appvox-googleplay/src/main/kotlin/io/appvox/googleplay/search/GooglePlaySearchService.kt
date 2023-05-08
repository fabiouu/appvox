package io.appvox.googleplay.search

import io.appvox.core.configuration.Constant.MAX_RETRY_ATTEMPTS
import io.appvox.core.configuration.Constant.MIN_RETRY_DELAY
import io.appvox.core.configuration.RequestConfiguration
import io.appvox.core.util.retryRequest
import io.appvox.googleplay.search.domain.GooglePlaySearch
import io.appvox.googleplay.search.domain.GooglePlaySearchRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@PublishedApi
internal class GooglePlaySearchService(val config: RequestConfiguration) {

    private val googlePlaySearchRepository = GooglePlaySearchRepository(config)

    fun searchAppByTerm(initialRequest: GooglePlaySearchRequest): Flow<GooglePlaySearch> = flow {
        var request = initialRequest
        do {
            val response = retryRequest(MAX_RETRY_ATTEMPTS, MIN_RETRY_DELAY) {
                googlePlaySearchRepository.searchAppByTerm(request)
            }
            request = request.copy(nextToken = response.nextToken)
            response.results?.forEach { result ->
                emit(result)
            }
            delay(timeMillis = config.delay.inWholeMilliseconds)
        } while (request.nextToken != null)
    }
}

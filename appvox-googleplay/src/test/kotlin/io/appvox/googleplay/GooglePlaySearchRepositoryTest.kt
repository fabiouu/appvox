package io.appvox.googleplay

import io.appvox.core.configuration.RequestConfiguration
import io.appvox.googleplay.review.constant.GooglePlayLanguage
import io.appvox.googleplay.search.GooglePlaySearchRepository
import io.appvox.googleplay.search.GooglePlaySearchRepository.Companion.APP_SEARCH_URL_PATH
import io.appvox.googleplay.search.GooglePlaySearchRepository.Companion.GOOGLE_PLAY_DOMAIN
import io.appvox.googleplay.search.domain.GooglePlaySearchRequest
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.time.Duration.Companion.seconds

class GooglePlaySearchRepositoryTest : BaseGooglePlayMockTest() {

    private val repository = GooglePlaySearchRepository(RequestConfiguration(delay = 3.seconds))

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "twitter, en-US, 1",
        "barcode, en-US, 30",
        "twitter, es-ES, 1",
        "barcode, es-ES, 30"
    )
    fun `get Google Play search results`(
        searchTerm: String,
        language: String,
        expectedResultCount: Int
    ) {
        GOOGLE_PLAY_DOMAIN = httpMockServerDomain
        val mockData = javaClass.getResource(
            "/search/$language/$searchTerm.html"
        ).readText()
        stubHttpUrl(APP_SEARCH_URL_PATH, mockData)

        val request = GooglePlaySearchRequest(
            searchTerm = searchTerm,
            language = GooglePlayLanguage.fromValue(language)
        )

        val response = repository.searchAppByTerm(request)

        response.results?.size shouldBe expectedResultCount
    }
}

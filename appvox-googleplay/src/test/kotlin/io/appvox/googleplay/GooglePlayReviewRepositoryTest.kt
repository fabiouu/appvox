package io.appvox.googleplay

import io.appvox.core.configuration.RequestConfiguration
import io.appvox.googleplay.review.GooglePlayReviewRepository
import io.appvox.googleplay.review.GooglePlayReviewRepository.Companion.REQUEST_URL_DOMAIN
import io.appvox.googleplay.review.GooglePlayReviewRepository.Companion.REQUEST_URL_PATH
import io.appvox.googleplay.review.constant.GooglePlayLanguage
import io.appvox.googleplay.review.constant.GooglePlaySortType
import io.appvox.googleplay.review.domain.GooglePlayReviewRequest
import io.kotest.assertions.assertSoftly
import io.kotest.inspectors.forExactly
import io.kotest.matchers.ints.shouldBeBetween
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.time.Duration.Companion.seconds

class GooglePlayReviewRepositoryTest : BaseGooglePlayMockTest() {

    private val repository = GooglePlayReviewRepository(RequestConfiguration(delay = 3.seconds))

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, en, 1, 40, 40"
    )
    fun `get Google Play reviews`(
        appId: String,
        language: String,
        sortType: Int,
        batchSize: Int,
        expectedReviewCount: Int
    ) {
        REQUEST_URL_DOMAIN = httpMockServerDomain
        val mockData = javaClass.getResource(
            "/reviews/$appId/review_relevant.html"
        ).readText()
        stubHttpUrl(REQUEST_URL_PATH, mockData)

        val request = GooglePlayReviewRequest(
            appId = appId,
            language = GooglePlayLanguage.fromValue(language),
            sortType = GooglePlaySortType.fromValue(sortType),
            batchSize = batchSize,
            fetchHistory = false
        )

        val response = repository.getReviewsByAppId(request)

        response.results?.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id shouldStartWith "gp:"
                username.shouldNotBeEmpty()
                avatar shouldStartWith "https://play-lh.googleusercontent.com/"
                rating.shouldBeBetween(1, 5)
                text.shouldNotBeEmpty()
//                latestUpdateTime.shouldBeBetween(0, Long.MAX_VALUE)
                likeCount.shouldBeGreaterThanOrEqual(0)
//                developerCommentText?.let { it.shouldNotBeEmpty() }
//                developerCommentTime?.shouldBeBetween(0, Long.MAX_VALUE)
            }
        }
        response.nextToken.shouldNotBeEmpty()
    }
}

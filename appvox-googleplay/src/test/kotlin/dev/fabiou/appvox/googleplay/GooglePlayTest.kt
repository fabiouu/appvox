package dev.fabiou.appvox.googleplay

import dev.fabiou.appvox.googleplay.app.GooglePlayRepository
import dev.fabiou.appvox.googleplay.review.GooglePlayReviewRepository.Companion.REQUEST_URL_DOMAIN
import dev.fabiou.appvox.googleplay.review.GooglePlayReviewRepository.Companion.REQUEST_URL_PATH
import dev.fabiou.appvox.googleplay.review.constant.GooglePlayLanguage
import dev.fabiou.appvox.googleplay.review.constant.GooglePlaySortType
import dev.fabiou.appvox.googleplay.review.domain.GooglePlayReview
import io.kotest.assertions.assertSoftly
import io.kotest.inspectors.forExactly
import io.kotest.matchers.ints.shouldBeBetween
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.contracts.ExperimentalContracts

class GooglePlayTest : BaseGooglePlayMockTest() {

    @ExperimentalContracts
    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, 50"
    )
    fun `get Google Play reviews using default optional parameters`(
        appId: String,
        expectedReviewCount: Int
    ) = runBlockingTest {

        GooglePlayRepository.APP_HP_URL_DOMAIN = httpMockServerDomain
        val scriptParamsMockData = javaClass.getResource(
            "/app/com.twitter.android" +
                "/app_googleplay_com.twitter.android_homepage.html"
        ).readText()
        stubHttpUrl(GooglePlayRepository.APP_HP_URL_PATH, scriptParamsMockData)

        REQUEST_URL_DOMAIN = httpMockServerDomain
        val mockData =
            javaClass.getResource(
                "/review/com.twitter.android/relevant" +
                    "/review_google_play_com.twitter.android_relevant_1.json"
            ).readText()
        stubHttpUrl(REQUEST_URL_PATH, mockData)

        val reviews = ArrayList<GooglePlayReview>()
        GooglePlay()
            .reviews { this.appId = appId }
            .take(expectedReviewCount)
            .collect { review ->
                reviews.add(review)
            }

        reviews.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id shouldStartWith "gp:"
                language.shouldNotBeNull()
                url shouldContain id
                userTypes.shouldNotBeNull()
                latestComment.shouldNotBeNull()
            }
            assertSoftly(result.latestUserComment) {
                name.shouldNotBeEmpty()
                avatar shouldStartWith "https://play-lh.googleusercontent.com/"
                rating.shouldBeBetween(1, 5)
                text.shouldNotBeEmpty()
                lastUpdateTime.shouldNotBeNull()
                likeCount.shouldBeGreaterThanOrEqual(0)
                types.shouldNotBeNull()
            }
            assertSoftly(result.latestDeveloperComment) {
                text?.let { it.shouldNotBeEmpty() }
            }
        }
    }

    @ExperimentalContracts
    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, en-US, 1, 50"
    )
    fun `Get most relevant Google Play reviews from the US with a delay of 3s between each request`(
        appId: String,
        language: String,
        sortType: Int,
        expectedReviewCount: Int
    ) = runBlockingTest {

        GooglePlayRepository.APP_HP_URL_DOMAIN = httpMockServerDomain
        val scriptParamsMockData = javaClass.getResource(
            "/app/com.twitter.android" +
                "/app_googleplay_com.twitter.android_homepage.html"
        ).readText()
        stubHttpUrl(GooglePlayRepository.APP_HP_URL_PATH, scriptParamsMockData)

        REQUEST_URL_DOMAIN = httpMockServerDomain
        val mockResponse =
            javaClass.getResource(
                "/review/com.twitter.android/relevant" +
                    "/review_google_play_com.twitter.android_relevant_1.json"
            ).readText()
        stubHttpUrl(REQUEST_URL_PATH, mockResponse)

        val reviews = ArrayList<GooglePlayReview>()
        val googlePlay = GooglePlay()
        googlePlay
            .reviews(
                appId = appId,
                language = GooglePlayLanguage.fromValue(language),
                sortType = GooglePlaySortType.fromValue(sortType)
            )
            .take(expectedReviewCount)
            .collect { review ->
                reviews.add(review)
            }

        reviews.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id shouldStartWith "gp:"
                language.shouldNotBeNull()
                url shouldContain id
                userTypes.shouldNotBeNull()
                latestComment.shouldNotBeNull()
            }
            assertSoftly(result.latestUserComment) {
                name.shouldNotBeEmpty()
                avatar shouldStartWith "https://play-lh.googleusercontent.com/"
                rating.shouldBeBetween(1, 5)
                text.shouldNotBeEmpty()
                lastUpdateTime.shouldNotBeNull()
                likeCount.shouldBeGreaterThanOrEqual(0)
                types.shouldNotBeNull()
            }
            assertSoftly(result.latestDeveloperComment) {
                text?.let { it.shouldNotBeEmpty() }
            }
        }
    }

    @ExperimentalContracts
    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, en-US, 1, 50"
    )
    fun `Get most relevant Google Play reviews from the US with a delay of 3s between each request built a DSL`(
        appId: String,
        language: String,
        sortType: Int,
        expectedReviewCount: Int
    ) = runBlockingTest {

        GooglePlayRepository.APP_HP_URL_DOMAIN = httpMockServerDomain
        val scriptParamsMockData = javaClass.getResource(
            "/app/com.twitter.android" +
                "/app_googleplay_com.twitter.android_homepage.html"
        ).readText()
        stubHttpUrl(GooglePlayRepository.APP_HP_URL_PATH, scriptParamsMockData)

        REQUEST_URL_DOMAIN = httpMockServerDomain
        val mockResponse =
            javaClass.getResource(
                "/review/com.twitter.android/relevant" +
                    "/review_google_play_com.twitter.android_relevant_1.json"
            ).readText()
        stubHttpUrl(REQUEST_URL_PATH, mockResponse)

        val reviews = ArrayList<GooglePlayReview>()
        val googlePlay = GooglePlay()
        googlePlay
            .reviews {
                this.appId = appId
                this.language = GooglePlayLanguage.fromValue(language)
                this.sortType = GooglePlaySortType.fromValue(sortType)
            }
            .take(expectedReviewCount)
            .collect { review ->
                reviews.add(review)
            }

        reviews.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id shouldStartWith "gp:"
                language.shouldNotBeNull()
                url shouldContain id
                userTypes.shouldNotBeNull()
                latestComment.shouldNotBeNull()
            }
            assertSoftly(result.latestUserComment) {
                name.shouldNotBeEmpty()
                avatar shouldStartWith "https://play-lh.googleusercontent.com/"
                rating.shouldBeBetween(1, 5)
                text.shouldNotBeEmpty()
                lastUpdateTime.shouldNotBeNull()
                likeCount.shouldBeGreaterThanOrEqual(0)
                types.shouldNotBeNull()
            }
            assertSoftly(result.latestDeveloperComment) {
                text?.let { it.shouldNotBeEmpty() }
            }
        }
    }
}

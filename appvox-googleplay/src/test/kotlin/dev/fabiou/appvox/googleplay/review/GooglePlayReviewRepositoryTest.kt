package io.appvox.googleplay.review

import io.appvox.configuration.RequestConfiguration
import io.appvox.googleplay.BaseGooglePlayMockTest
import io.appvox.googleplay.review.GooglePlayReviewRepository.Companion.REQUEST_URL_DOMAIN
import io.appvox.googleplay.review.GooglePlayReviewRepository.Companion.REQUEST_URL_PATH
import io.appvox.googleplay.review.constant.GooglePlayLanguage
import io.appvox.googleplay.review.constant.GooglePlaySortType
import io.appvox.googleplay.review.domain.GooglePlayReviewRequestParameters
import io.appvox.review.ReviewRequest
import io.kotest.assertions.assertSoftly
import io.kotest.inspectors.forExactly
import io.kotest.matchers.ints.shouldBeBetween
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeBetween
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GooglePlayReviewRepositoryTest : BaseGooglePlayMockTest() {

    private val repository = GooglePlayReviewRepository(RequestConfiguration(delay = 3000))

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
            "/review/com.twitter.android/relevant" +
                "/review_google_play_com.twitter.android_relevant_1.json"
        ).readText()
        stubHttpUrl(REQUEST_URL_PATH, mockData)

        val request = GooglePlayReviewRequestParameters(
            appId = appId,
            language = GooglePlayLanguage.fromValue(language),
            sortType = GooglePlaySortType.fromValue(sortType),
            batchSize = batchSize,
            fetchHistory = false
        )

        val response = repository.getReviewsByAppId(ReviewRequest(request))

        response.results.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                reviewId shouldStartWith "gp:"
                userName.shouldNotBeEmpty()
                userProfilePicUrl shouldStartWith "https://play-lh.googleusercontent.com/"
                rating.shouldBeBetween(1, 5)
                userCommentText.shouldNotBeEmpty()
                userCommentTime.shouldBeBetween(0, Long.MAX_VALUE)
                likeCount.shouldBeGreaterThanOrEqual(0)
                reviewUrl shouldContain result.reviewId
                developerCommentText?.let { it.shouldNotBeEmpty() }
                developerCommentTime?.shouldBeBetween(0, Long.MAX_VALUE)
            }
        }
        response.nextToken.shouldNotBeEmpty()
    }

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, " +
        "gp:AOqpTOFeIsixb5qcUUUvJLSz_JudDjdKvngeHbfbUNGh7ch4H3KYh6NFVObMQkdes5HXbLkp3x5iyEiyRsTpuw, " +
        "en, " +
        "40, " +
        "2"
    )
    fun `get Google Play review history`(
        appId: String,
        reviewId: String,
        language: String,
        batchSize: Int,
        expectedReviewCount: Int
    ) {
        REQUEST_URL_DOMAIN = httpMockServerDomain
        val mockData = javaClass.getResource(
            "/review/com.twitter.android/history" +
                "/review_google_play_com.twitter.android_history.json"
        ).readText()
        stubHttpUrl(REQUEST_URL_PATH, mockData)

        val request = GooglePlayReviewRequestParameters(
            appId = appId,
            language = GooglePlayLanguage.fromValue(language),
            // Sort Type is not used when fetching a review's history
            sortType = GooglePlaySortType.RECENT,
            batchSize = batchSize,
            fetchHistory = true
        )

        val response = repository.getReviewHistoryById(reviewId, ReviewRequest(request))
        response.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                reviewId shouldStartWith "gp:"
                userName.shouldNotBeEmpty()
                userProfilePicUrl shouldStartWith "https://play-lh.googleusercontent.com/"
                rating.shouldBeBetween(1, 5)
                userCommentText.shouldNotBeEmpty()
                userCommentTime.shouldBeBetween(0, Long.MAX_VALUE)
                likeCount.shouldBeGreaterThanOrEqual(0)
                reviewUrl shouldContain result.reviewId
            }
        }
    }
}

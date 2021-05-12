package dev.fabiou.appvox

import dev.fabiou.appvox.review.googleplay.GooglePlayReviewRepository.Companion.REQUEST_URL_DOMAIN
import dev.fabiou.appvox.review.googleplay.GooglePlayReviewRepository.Companion.REQUEST_URL_PATH
import dev.fabiou.appvox.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.review.googleplay.constant.GooglePlaySortType
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview
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

class GooglePlayTest : BaseMockTest() {

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
        REQUEST_URL_DOMAIN = httpMockServerDomain
        val mockData =
            javaClass.getResource("/review/google_play/com.twitter.android/relevant/review_google_play_com.twitter.android_relevant_1.json")
                .readText()
        stubHttpUrl(REQUEST_URL_PATH, mockData)

        val reviews = ArrayList<GooglePlayReview>()
        GooglePlay()
            .reviews(appId)
            .take(expectedReviewCount)
            .collect { review ->
                reviews.add(review)
            }

        reviews.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id shouldStartWith "gp:"
                userName.shouldNotBeEmpty()
                userAvatar shouldStartWith "https://play-lh.googleusercontent.com/"
                rating.shouldBeBetween(1, 5)
                latestComment.comment.shouldNotBeEmpty()
                latestComment.commentTime.shouldNotBeNull()
                likeCount.shouldBeGreaterThanOrEqual(0)
                url shouldContain id
                replyComment?.let { it.shouldNotBeEmpty() }
                // TODO replyTime?.let { it.shouldNotBeNull() }
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
        REQUEST_URL_DOMAIN = httpMockServerDomain
        val mockResponse =
            javaClass.getResource("/review/google_play/com.twitter.android/relevant/review_google_play_com.twitter.android_relevant_1.json")
                .readText()
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
                userName.shouldNotBeEmpty()
                userAvatar shouldStartWith "https://play-lh.googleusercontent.com/"
                rating.shouldBeBetween(1, 5)
                latestComment.comment.shouldNotBeEmpty()
                latestComment.commentTime.shouldNotBeNull()
                likeCount.shouldBeGreaterThanOrEqual(0)
                url shouldContain id
                replyComment?.let { it.shouldNotBeEmpty() }
                // TODO replyTime?.let { it.shouldNotBeNull() }
            }
        }
    }
}

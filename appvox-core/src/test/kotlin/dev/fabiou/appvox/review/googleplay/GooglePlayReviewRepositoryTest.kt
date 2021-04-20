package dev.fabiou.appvox.review.googleplay

import UrlUtil
import dev.fabiou.appvox.BaseMockTest
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.googleplay.GooglePlayReviewRepository.Companion.REQUEST_URL_DOMAIN
import dev.fabiou.appvox.review.googleplay.GooglePlayReviewRepository.Companion.REQUEST_URL_PATH
import dev.fabiou.appvox.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.review.googleplay.constant.GooglePlaySortType
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewRequest
import io.kotest.assertions.assertSoftly
import io.kotest.inspectors.forExactly
import io.kotest.matchers.ints.shouldBeBetween
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeBetween
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GooglePlayReviewRepositoryTest : BaseMockTest() {

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
        REQUEST_URL_DOMAIN = UrlUtil.getUrlDomainByEnv(REQUEST_URL_DOMAIN)
        val mockData =
            javaClass.getResource("/review/google_play/com.twitter.android/relevant/review_google_play_com.twitter.android_relevant_1.json")
                .readText()
        stubHttpUrl(REQUEST_URL_PATH, mockData)

        val request = GooglePlayReviewRequest(
            appId = appId,
            language = GooglePlayLanguage.fromValue(language),
            sortType = GooglePlaySortType.fromValue(sortType),
            batchSize = batchSize,
        )

        val response = repository.getReviewsByAppId(ReviewRequest(request))

        response.results.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                reviewId shouldStartWith "gp:"
                userName.shouldNotBeEmpty()
                userProfilePicUrl shouldStartWith "https://play-lh.googleusercontent.com/"
                rating.shouldBeBetween(1, 5)
                comment.shouldNotBeEmpty()
                submitTime.shouldBeBetween(0, Long.MAX_VALUE)
                likeCount.shouldBeGreaterThanOrEqual(0)
                reviewUrl shouldContain result.reviewId
                replyComment?.let { it.shouldNotBeEmpty() }
                replySubmitTime?.let { it.shouldBeBetween(0, Long.MAX_VALUE) }
            }
        }
        response.nextToken.shouldNotBeEmpty()
    }
}

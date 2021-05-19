package dev.fabiou.appvox.review.googleplay

import dev.fabiou.appvox.BaseMockTest
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewHistoryRequestParameters
import io.kotest.assertions.assertSoftly
import io.kotest.inspectors.forExactly
import io.kotest.matchers.ints.shouldBeBetween
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeBetween
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GooglePlayReviewHistoryRepositoryTest : BaseMockTest() {

    private val repository = GooglePlayReviewHistoryRepository(RequestConfiguration(delay = 3000))

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, en, 40, gp:AOqpTOFeIsixb5qcUUUvJLSz_JudDjdKvngeHbfbUNGh7ch4H3KYh6NFVObMQkdes5HXbLkp3x5iyEiyRsTpuw, 2"
    )
    fun `get Google Play review history`(
        appId: String,
        language: String,
        batchSize: Int,
        reviewId: String,
        expectedReviewCount: Int,
    ) {
        val request = GooglePlayReviewHistoryRequestParameters(
            appId = appId,
            language = GooglePlayLanguage.fromValue(language),
            batchSize = batchSize,
            reviewId = reviewId,

            sid = "2490772921985188942",
            bl = "boq_playuiserver_20210517.01_p0"
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
            }
        }
        response.nextToken.shouldNotBeEmpty()
    }
}

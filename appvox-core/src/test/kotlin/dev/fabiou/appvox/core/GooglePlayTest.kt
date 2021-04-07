package dev.fabiou.appvox.core

import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.review.googleplay.GooglePlayReviewRepository
import dev.fabiou.appvox.core.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.review.googleplay.constant.GooglePlaySortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GooglePlayTest : BaseRepositoryTest() {

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, 50"
    )
    fun `Get Google Play reviews using default optional parameters`(
        appId: String,
        maxReviewCount: Int
    ) = runBlockingTest {

        val mockData = javaClass.getResource("/review/googleplay_reviews_mock_data.json").readText()
        stubHttpUrl(GooglePlayReviewRepository.REQUEST_URL_PATH, mockData)

        var fetchedReviewCount = 0
        val googlePlay = GooglePlay()
        googlePlay.reviews(appId)
            .take(maxReviewCount)
            .collect { _ ->
                fetchedReviewCount++
            }

        Assertions.assertEquals(maxReviewCount, fetchedReviewCount)
    }

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, en-US, 1, 50"
    )
    fun `Get most relevant Google Play reviews from the US with a delay of 3s between each network call`(
        appId: String,
        language: String,
        sortType: Int,
        maxReviewCount: Int
    ) = runBlockingTest {

        val mockData = javaClass.getResource("/review/googleplay_reviews_mock_data.json").readText()
        stubHttpUrl(GooglePlayReviewRepository.REQUEST_URL_PATH, mockData)

        var fetchedReviewCount = 0
        val googlePlay = GooglePlay(RequestConfiguration(requestDelay = 3000))
        googlePlay.reviews(
            appId = appId,
            language = GooglePlayLanguage.fromValue(language),
            sortType = GooglePlaySortType.fromValue(sortType)
        )
            .take(maxReviewCount)
            .collect { _ ->
                fetchedReviewCount++
            }

        Assertions.assertEquals(maxReviewCount, fetchedReviewCount)
    }
}

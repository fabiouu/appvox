package dev.fabiou.appvox.core

import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.review.googleplay.GooglePlayReviewRepository
import dev.fabiou.appvox.core.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.review.googleplay.constant.GooglePlaySortType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GooglePlayTest : BaseRepositoryTest() {

    @ParameterizedTest
    @CsvSource(
            "com.twitter.android, 50"
    )
    fun `Get Google Play reviews using iterator and minimal parameters`(
            appId: String,
            requestedReviewCount: Int) {

        val mockData = javaClass.getResource("/googleplay_reviews_mock_data.json").readText()
        stubHttpUrl(GooglePlayReviewRepository.REQUEST_URL_PATH, mockData)

        var fetchedReviewCount = 0
        val googlePlay = GooglePlay()
        googlePlay.reviews(appId)
                .forEach { _ ->
                    fetchedReviewCount++
                }

        Assertions.assertEquals(requestedReviewCount, fetchedReviewCount)
    }

    @ParameterizedTest
    @CsvSource(
            "com.twitter.android, en-US, 1, 50"
    )
    fun `Get most relevant Google Play reviews using iterator by batch of N reviews`(
            appId: String,
            language: String,
            sortType: Int,
            maxReviewCount: Int) {

        val mockData = javaClass.getResource("/googleplay_reviews_mock_data.json").readText()
        stubHttpUrl(GooglePlayReviewRepository.REQUEST_URL_PATH, mockData)

        var fetchedReviewCount = 0
        val googlePlay = GooglePlay(RequestConfiguration(requestDelay = 3000))
        googlePlay.reviews(
                appId = appId,
                language = GooglePlayLanguage.fromValue(language),
                sortType = GooglePlaySortType.fromValue(sortType),
                maxCount = maxReviewCount)
                .forEach { _ ->
                    fetchedReviewCount++
                }

        Assertions.assertEquals(maxReviewCount, fetchedReviewCount)
    }
}
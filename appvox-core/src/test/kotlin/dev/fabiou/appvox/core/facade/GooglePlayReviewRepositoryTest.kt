package dev.fabiou.appvox.core.facade

import dev.fabiou.appvox.core.BaseRepositoryTest
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.googleplay.review.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.googleplay.review.constant.GooglePlaySortType
import dev.fabiou.appvox.core.googleplay.review.domain.GooglePlayReviewRequest
import dev.fabiou.appvox.core.googleplay.review.GooglePlayReviewRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GooglePlayReviewRepositoryTest : BaseRepositoryTest() {

    private var service = GooglePlayReviewRepository(RequestConfiguration(requestDelay = 3000L))

    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, en, newest, 40, 40"
    )
    fun `Get Google Play reviews`(
        appId: String,
        language: String,
        sortType: String,
        batchSize: Int,
        requestedSize: Int) {

        val mockData = javaClass.getResource("/googleplay_reviews_mock_data.json").readText()
        stubHttpUrl(GooglePlayReviewRepository.REQUEST_URL_PATH, mockData)

        val request = GooglePlayReviewRequest(
            appId = appId,
            language = GooglePlayLanguage.fromValue(language),
            sortType = GooglePlaySortType.fromValue(sortType),
            batchSize = batchSize
        )

        val response = service.getReviewsByAppId(request)

        Assertions.assertEquals(requestedSize, response.reviews.size)
    }
}
package dev.fabiou.appvox.core.review.repository

import dev.fabiou.appvox.core.BaseStoreServiceTest
import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.review.constant.GooglePlaySortType
import dev.fabiou.appvox.core.review.domain.request.GooglePlayReviewRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GooglePlayReviewRepositoryTest : BaseStoreServiceTest() {

    private var service = GooglePlayReviewRepository(Configuration(requestDelay = 3000L))

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
            batchSize = batchSize)

        val response = service.getReviewsByAppId(request)

        Assertions.assertEquals(requestedSize, response.reviews.size)
    }
}
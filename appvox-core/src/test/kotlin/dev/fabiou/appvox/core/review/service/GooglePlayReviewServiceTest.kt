package dev.fabiou.appvox.core.review.service

import dev.fabiou.appvox.core.BaseStoreServiceTest
import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.review.constant.GooglePlaySortType
import dev.fabiou.appvox.core.review.domain.request.GooglePlayReviewRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GooglePlayReviewServiceTest : BaseStoreServiceTest() {

    private var service = GooglePlayReviewService(Configuration(requestDelay = 3000L))

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
        stubHttpUrl(GooglePlayReviewService.REQUEST_URL_PATH, mockData)

        val request = GooglePlayReviewRequest(
            language = GooglePlayLanguage.fromValue(language),
            sortType = GooglePlaySortType.fromValue(sortType),
            batchSize = batchSize)

        val response = service.getReviewsByAppId(appId, request)

        Assertions.assertEquals(requestedSize, response.reviews.size)
    }
}
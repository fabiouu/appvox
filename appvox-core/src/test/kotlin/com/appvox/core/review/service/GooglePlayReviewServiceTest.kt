package com.appvox.core.review.service

import com.appvox.core.configuration.Configuration
import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.constant.GooglePlayLanguage
import com.appvox.core.review.constant.GooglePlaySortType
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GooglePlayReviewServiceTest {

    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, en, 1, 40, 200, 40"
    )
    fun `Get Google Play reviews`(
        appId: String,
        language: String,
        sortType: Int, batchSize: Int, expectedResponseStatus: Int, expectedBatchSize: Int) {
        val config = Configuration(
            proxy = ProxyConfiguration(
                host = "127.0.0.1",
                port = 1087),
            requestDelay = 3000
        )
        val service = GooglePlayReviewService(config)
        val request = GooglePlayReviewRequest(
            language = GooglePlayLanguage.ENGLISH_UK,
            sortType = GooglePlaySortType.NEWEST,
            batchSize = batchSize)

        val response = service.getReviewsByAppId(appId, request)

        Assertions.assertEquals(expectedBatchSize, response.reviews.size)
    }
}
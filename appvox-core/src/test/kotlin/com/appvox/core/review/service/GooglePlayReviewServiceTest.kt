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
        "com.twitter.android, en, newest, 40, 40"
    )
    fun `Get Google Play reviews`(
        appId: String,
        language: String,
        sortType: String,
        batchSize: Int,
        requestedSize: Int) {
        val service = GooglePlayReviewService()
        val request = GooglePlayReviewRequest(
            language = GooglePlayLanguage.fromValue(language),
            sortType = GooglePlaySortType.fromValue(sortType),
            batchSize = batchSize)

        val response = service.getReviewsByAppId(appId, request)

        Assertions.assertEquals(requestedSize, response.reviews.size)
    }
}
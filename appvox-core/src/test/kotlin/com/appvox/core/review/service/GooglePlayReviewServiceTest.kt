package com.appvox.core.review.service

import com.appvox.core.configuration.Configuration
import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import com.appvox.core.review.service.AppStoreReviewService
import com.appvox.core.review.service.GooglePlayReviewService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GooglePlayReviewServiceSpec {

    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, en, 1, 40, 200, 40"
    )
    fun `Get google play reviews`(
        appId: String, language: String, sort: Int, size: Int, expectedResponseStatus: Int, expectedSize: Int) {
        val request = GooglePlayReviewRequest(language, sort, size, "", "")

        val configuration = Configuration.Builder().proxy(
            ProxyConfiguration.Builder()
                .host("127.0.0.1")
                .port(1087)
                .build()
        ).build();
        val service = GooglePlayReviewService(configuration)
        val response = service.getReviewsByAppId(appId, request)

        Assertions.assertEquals(response.reviews.size, expectedSize)
    }
}
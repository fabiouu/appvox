package com.appvox.core.review.service

import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.constant.GooglePlaySortType
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
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

        val proxy = ProxyConfiguration(
            host = "127.0.0.1",
            port = 1087
        )
        val service = GooglePlayReviewService(proxy)

//        val service = GooglePlayReviewService()
        val request = GooglePlayReviewRequest(language, GooglePlaySortType.NEWEST, size)

        val response = service.getReviewsByAppId(appId, request)

        Assertions.assertEquals(response.reviews.size, expectedSize)
    }
}
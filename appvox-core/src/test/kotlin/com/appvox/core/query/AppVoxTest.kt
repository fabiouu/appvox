package com.appvox.core.query

import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.service.AppStoreReviewService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppVoxTest {

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 1, 0, 200, 10"
    )
    fun `Get app store reviews using iterator`(
        appId: String,
        region: String,
        sort: Int,
        size: Int,
        expectedResponseCode: Int,
        expectedSize: Int) {

        val proxy = ProxyConfiguration(host = "127.0.0.1", port = 1087)
        val appVox = AppVox(proxy)
        appVox
            .appStoreReviews(appId = "785385147", region = region)
            .forEach { review ->
                println("Review Text: " + review.comment)
            }
    }

    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, en, 1, 40, 200, 40"
    )
    fun `Get google play reviews using iterator`(
        appId: String,
        language: String,
        sort: Int,
        size: Int,
        expectedResponseStatus: Int,
        expectedSize: Int) {

        val proxy = ProxyConfiguration(host = "127.0.0.1", port = 1087)
        val appVox = AppVox(proxy)
        appVox
            .googlePlayReviews(appId = appId, language = language, sortType = sort, size = size)
            .forEach { review ->
                println("Review Text: " + review.comment)
            }
    }

}
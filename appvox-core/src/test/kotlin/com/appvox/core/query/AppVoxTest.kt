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

//        val request = AppStoreReviewRequest(region, size, "", "")
        val proxyConfig = ProxyConfiguration(host = "127.0.0.1", port = 1090)
        val appVox = AppVox(proxyConfig)
        appVox
                .appStoreReviews(appId = "785385147")
                .forEach { review ->
                    println("Comment: " + review.comment)
                }

//        Assertions.assertEquals(expectedSize, response?.data?.size)
    }

}
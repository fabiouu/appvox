package com.appvox.core.review.service

import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreReviewServiceSpec {

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 200, 10"
    )
    fun `Get app store reviews`(
        appId : String, region : String, expectedResponseCode : Int, expectedSize : Int) {

        val proxy = ProxyConfiguration(
            host = "127.0.0.1",
            port = 1087
        )
        val service = AppStoreReviewService(proxy)

//        val service = AppStoreReviewService()
        val bearerToken = service.getBearerToken(
            appId = appId,
            region = region
        )
        val request = AppStoreReviewRequest(
                region = region,
                bearerToken = bearerToken
        )

        val response = service.getReviewsByAppId(appId, request)

        Assertions.assertEquals(expectedSize, response?.data?.size)
    }
}
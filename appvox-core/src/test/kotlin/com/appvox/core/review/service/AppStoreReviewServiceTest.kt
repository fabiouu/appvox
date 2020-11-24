package com.appvox.core.review.service

import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreReviewServiceSpec {

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 1, 0, 200, 10"
    )
    fun `Get app store reviews`(
        appId : String, region : String, sort : Int, size : Int, expectedResponseCode : Int, expectedSize : Int) {
        val request = AppStoreReviewRequest(region, size, "", "")

        val service = AppStoreReviewService()
        val response = service.getReviewsByAppId(appId, request)

        Assertions.assertEquals(expectedSize, response?.data?.size)
    }

    @ParameterizedTest
    @CsvSource(
            "333903271, us"
    )
    fun `Get app store authentication token`(appId : String, region : String) {
//        val configuration = com.appvox.core.configuration.Configuration.Builder()
//                .proxy(ProxyConfiguration.Builder()
//                        .host("127.0.0.1")
//                        .port(1090)
//                        .build()).build();
//        val service = AppStoreReviewService(configuration)
//        val bearerToken = service.getBearerToken(appId, region)
//        println(bearerToken)
    }
}
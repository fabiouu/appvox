package com.appvox.core.review.service

import com.appvox.core.configuration.Configuration
import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreReviewServiceTest {

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 10"
    )
    fun `Get app store reviews`(
        appId : String, region : String, requestedSize : Int) {

        val service = AppStoreReviewService()
        val bearerToken = service.getBearerToken(
            appId = appId,
            region = region
        )
        val request = AppStoreReviewRequest(
                region = region,
                bearerToken = bearerToken
        )

        val response = service.getReviewsByAppId(appId, request)

        Assertions.assertEquals(requestedSize, response?.data?.size)
    }
}
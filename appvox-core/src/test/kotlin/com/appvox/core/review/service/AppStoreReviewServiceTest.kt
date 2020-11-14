package com.appvox.core.review.service

import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.service.AppStoreReviewService
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

        Assertions.assertEquals(response?.data?.size, expectedSize)
    }
}
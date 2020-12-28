package com.appvox.core.review.service

import com.appvox.core.configuration.Configuration
import com.appvox.core.review.constant.AppStoreSortType
import com.appvox.core.review.domain.request.AppStoreReviewRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreRecentReviewServiceTest {

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 50"
    )
    fun `Get most recent App Store reviews from itunes RSS Feed`(
        appId: String, region: String, requestedSize: Int) {
        val config = Configuration(requestDelay = 3000L)
        val service = AppStoreRecentReviewService(config)
        val request = AppStoreReviewRequest(
            region = region,
            sortType = AppStoreSortType.RECENT,
            pageNo = 1
        )

        val response = service.getReviewsByAppId(appId, request)

        Assertions.assertEquals(requestedSize, response?.entry?.size)
    }
}
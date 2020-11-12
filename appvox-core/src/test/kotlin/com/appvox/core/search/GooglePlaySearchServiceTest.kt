package com.appvox.core.search

import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import com.appvox.core.review.service.AppStoreReviewService
import com.appvox.core.review.service.GooglePlayReviewService
import com.appvox.core.search.domain.request.GooglePlaySearchRequest
import com.appvox.core.search.service.GooglePlaySearchService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GooglePlayReviewServiceSpec {

    @ParameterizedTest
    @CsvSource(
        "twitter, en, 50"
    )
    fun `Search Google Play App by name`(
        appName: String, language: String, expectedSize : Int) {
        val request = GooglePlaySearchRequest(appName, language, "", "")
        val response = GooglePlaySearchService.searchAppByName(request)
        Assertions.assertEquals(response.apps.size, expectedSize)
    }
}
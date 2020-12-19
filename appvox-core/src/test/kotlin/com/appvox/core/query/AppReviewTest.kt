package com.appvox.core.query

import com.appvox.core.configuration.Configuration
import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.constant.AppStoreSortType
import com.appvox.core.review.constant.GooglePlayLanguage
import com.appvox.core.review.constant.GooglePlayLanguage.*
import com.appvox.core.review.constant.GooglePlaySortType.*
import com.appvox.core.review.query.AppReview
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppReviewTest {

    @ParameterizedTest
    @CsvSource(
            "333903271, us, 50"
    )
    fun `Get most relevant App Store reviews using iterator`(
            appId: String,
            region: String,
            requestedFetchReviewCount: Int) {
        val config = Configuration(requestDelay = 3000)
        var fetchedReviewCount = 0
        val appReview = AppReview(config)
        appReview
                .appStore(
                        appId = "785385147",
                        region = region,
                        sortType = AppStoreSortType.RELEVANT,
                        fetchCountLimit = requestedFetchReviewCount)
                .forEach {
                    fetchedReviewCount++
                }

        Assertions.assertEquals(requestedFetchReviewCount, fetchedReviewCount)
    }

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 50"
    )
    fun `Get most recent App Store reviews using iterator`(
        appId: String,
        region: String,
        requestedFetchReviewCount: Int) {
        var fetchedReviewCount = 0
        val config = Configuration(requestDelay = 3000)
        val appReview = AppReview(config)
        appReview
            .appStore(
                appId = "785385147",
                region = region,
                sortType = AppStoreSortType.RECENT,
                fetchCountLimit = requestedFetchReviewCount)
            .forEach {
                fetchedReviewCount++
            }

        Assertions.assertEquals(requestedFetchReviewCount, fetchedReviewCount)
    }

    @ParameterizedTest
    @CsvSource(
            "com.twitter.android, 50"
    )
    fun `Get Google Play reviews using iterator and minimal parameters`(
            appId: String,
            requestedFetchReviewCount: Int) {

        var fetchedReviewCount = 0
        AppReview()
                .googlePlay(
                        appId = appId,
                        language = ENGLISH_US,
                        fetchCountLimit = requestedFetchReviewCount)
                .forEach {
                    fetchedReviewCount++
                }

        Assertions.assertEquals(requestedFetchReviewCount, fetchedReviewCount)
    }

    @ParameterizedTest
    @CsvSource(
            "com.twitter.android, en-US, 1, 44, 50"
    )
    fun `Get most relevant Google Play reviews using iterator by batch of N reviews`(
            appId: String,
            language: String,
            sortType: Int,
            batchSize: Int,
            requestedFetchReviewCount: Int) {

        var fetchedReviewCount = 0
        AppReview()
                .googlePlay(
                        appId = appId,
                        language = GooglePlayLanguage.fromValue(language),
                        sortType = RELEVANT,
                        fetchCountLimit = requestedFetchReviewCount,
                        batchSize = batchSize)
                .forEach {
                    fetchedReviewCount++
                }

        Assertions.assertEquals(requestedFetchReviewCount, fetchedReviewCount)
    }
}
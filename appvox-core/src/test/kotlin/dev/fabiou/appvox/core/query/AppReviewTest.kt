package dev.fabiou.appvox.core.query

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.constant.AppStoreSortType
import dev.fabiou.appvox.core.review.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.review.constant.GooglePlayLanguage.ENGLISH_US
import dev.fabiou.appvox.core.review.constant.GooglePlaySortType.RELEVANT
import dev.fabiou.appvox.core.review.query.AppReview
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppReviewTest {

    private var appReview = AppReview(Configuration(requestDelay = 3000))

    @ParameterizedTest
    @CsvSource(
            "333903271, us, 50"
    )
    fun `Get most relevant App Store reviews using iterator`(
            appId: String,
            region: String,
            requestedFetchReviewCount: Int) {

        var fetchedReviewCount = 0
        appReview
                .appStore(
                        appId = "785385147",
                        region = region,
                        sortType = AppStoreSortType.RELEVANT,
                        fetchCountLimit = requestedFetchReviewCount)
                .forEach { _ ->
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
        appReview.appStore(
                appId = "785385147",
                region = region,
                sortType = AppStoreSortType.RECENT,
                fetchCountLimit = requestedFetchReviewCount)
            .forEach { _ ->
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
        AppReview().googlePlay(
                        appId = appId,
                        language = ENGLISH_US,
                        fetchCountLimit = requestedFetchReviewCount)
                .forEach { _ ->
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
        AppReview().googlePlay(
                        appId = appId,
                        language = GooglePlayLanguage.fromValue(language),
                        sortType = RELEVANT,
                        fetchCountLimit = requestedFetchReviewCount,
                        batchSize = batchSize)
                .forEach { _ ->
                    fetchedReviewCount++
                }

        Assertions.assertEquals(requestedFetchReviewCount, fetchedReviewCount)
    }
}
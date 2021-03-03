package dev.fabiou.appvox.core.query

import dev.fabiou.appvox.core.BaseStoreServiceTest
import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.constant.AppStoreSortType
import dev.fabiou.appvox.core.review.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.review.constant.GooglePlayLanguage.ENGLISH_US
import dev.fabiou.appvox.core.review.constant.GooglePlaySortType.RELEVANT
import dev.fabiou.appvox.core.review.domain.request.AppStoreReviewRequest
import dev.fabiou.appvox.core.review.domain.request.GooglePlayReviewRequest
import dev.fabiou.appvox.core.review.query.AppReview
import dev.fabiou.appvox.core.review.service.AppStoreRecentReviewService
import dev.fabiou.appvox.core.review.service.AppStoreReviewService
import dev.fabiou.appvox.core.review.service.GooglePlayReviewService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppReviewTest : BaseStoreServiceTest() {

    private var appReview = AppReview(Configuration(requestDelay = 3000))

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 10"
    )
    fun `Get most relevant App Store reviews using iterator`(
        appId: String,
        region: String,
        requestedReviewCount: Int) {

        stubHttpUrl(AppStoreReviewService.APP_HP_URL_PATH.format(region, appId), "mock-bearer-token")
        val mockData = javaClass.getResource("/appstore_reviews_mock_data.json").readText()
        stubHttpUrl(AppStoreReviewService.REQUEST_URL_PATH.format(region, appId, AppStoreReviewService.REQUEST_REVIEW_SIZE), mockData)

        val request = AppStoreReviewRequest(
            appId = appId,
            region = region,
            sortType = AppStoreSortType.RELEVANT,
            maxCount = requestedReviewCount)

        var fetchedReviewCount = 0
        appReview.appStore(request)
            .forEach { _ ->
                fetchedReviewCount++
            }

        Assertions.assertEquals(requestedReviewCount, fetchedReviewCount)
    }

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 1, 50"
    )
    fun `Get most recent App Store reviews using iterator`(
        appId: String,
        region: String,
        pageNo: Int,
        requestedReviewCount: Int) {

        val mockData = javaClass.getResource("/appstore_recent_reviews_rss_mock_data.xml").readText()
        stubHttpUrl(AppStoreRecentReviewService.RSS_REQUEST_URL_PATH.format(region, pageNo, appId), mockData)

        val request = AppStoreReviewRequest(
            appId = appId,
            region = region,
            sortType = AppStoreSortType.RECENT,
            maxCount = requestedReviewCount)

        var fetchedReviewCount = 0
        appReview.appStore(request)
            .forEach { _ ->
                fetchedReviewCount++
            }

        Assertions.assertEquals(requestedReviewCount, fetchedReviewCount)
    }

    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, 50"
    )
    fun `Get Google Play reviews using iterator and minimal parameters`(
        appId: String,
        requestedReviewCount: Int) {

        val mockData = javaClass.getResource("/googleplay_reviews_mock_data.json").readText()
        stubHttpUrl(GooglePlayReviewService.REQUEST_URL_PATH, mockData)

        val request = GooglePlayReviewRequest(
            appId = appId,
            language = ENGLISH_US,
            maxCount = requestedReviewCount)

        var fetchedReviewCount = 0
        appReview.googlePlay(request)
            .forEach { _ ->
                fetchedReviewCount++
            }

        Assertions.assertEquals(requestedReviewCount, fetchedReviewCount)
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
        requestedReviewCount: Int) {

        val mockData = javaClass.getResource("/googleplay_reviews_mock_data.json").readText()
        stubHttpUrl(GooglePlayReviewService.REQUEST_URL_PATH, mockData)

        val request = GooglePlayReviewRequest(
            appId = appId,
            language = GooglePlayLanguage.fromValue(language),
            sortType = RELEVANT,
            batchSize = batchSize,
            maxCount = requestedReviewCount)

        var fetchedReviewCount = 0
        appReview.googlePlay(request)
            .forEach { _ ->
                fetchedReviewCount++
            }

        Assertions.assertEquals(requestedReviewCount, fetchedReviewCount)
    }
}
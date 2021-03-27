package dev.fabiou.appvox.core.facade

import dev.fabiou.appvox.core.BaseRepositoryTest
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.googleplay.review.constant.AppReviewSortType
import dev.fabiou.appvox.core.googleplay.review.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.googleplay.review.constant.GooglePlayLanguage.ENGLISH_US
import dev.fabiou.appvox.core.archive.AppReviewRequest
import dev.fabiou.appvox.core.GooglePlay
import dev.fabiou.appvox.core.appstore.review.AppStoreReviewRepository
import dev.fabiou.appvox.core.googleplay.review.GooglePlayReviewRepository
import dev.fabiou.appvox.core.appstore.review.ItunesRssReviewRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppVoxTest : BaseRepositoryTest() {

    private var appVox = GooglePlay(RequestConfiguration(requestDelay = 3000))

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 10"
    )
    fun `Get most relevant App Store reviews using iterator`(
        appId: String,
        region: String,
        requestedReviewCount: Int) {

        stubHttpUrl(AppStoreReviewRepository.APP_HP_URL_PATH.format(region, appId), "mock-bearer-token")
        val mockData = javaClass.getResource("/appstore_reviews_mock_data.json").readText()
        stubHttpUrl(AppStoreReviewRepository.REQUEST_URL_PATH.format(region, appId, AppStoreReviewRepository.REQUEST_REVIEW_SIZE), mockData)

        val request = AppReviewRequest(
            appId = appId,
            region = region,
            sortType = AppReviewSortType.RELEVANT,
            maxCount = requestedReviewCount)

        var fetchedReviewCount = 0
        appVox.reviews(request)
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
        stubHttpUrl(ItunesRssReviewRepository.RSS_REQUEST_URL_PATH.format(region, pageNo, appId), mockData)

        val request = AppReviewRequest(
            appId = appId,
            region = region,
            sortType = AppReviewSortType.RECENT,
            maxCount = requestedReviewCount)

        var fetchedReviewCount = 0
        appVox.reviews(request)
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
        stubHttpUrl(GooglePlayReviewRepository.REQUEST_URL_PATH, mockData)

        val request = AppReviewRequest(
            appId = appId,
            region = "us",
            language = ENGLISH_US,
            maxCount = requestedReviewCount)

        var fetchedReviewCount = 0
        appVox.reviews(request)
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
        stubHttpUrl(GooglePlayReviewRepository.REQUEST_URL_PATH, mockData)

        val request = AppReviewRequest(
            appId = appId,
            region = "us",
            language = GooglePlayLanguage.fromValue(language),
            sortType = AppReviewSortType.RELEVANT,
//            batchSize = batchSize,
            maxCount = requestedReviewCount)

        var fetchedReviewCount = 0
        appVox.reviews(request)
            .forEach { _ ->
                fetchedReviewCount++
            }

        Assertions.assertEquals(requestedReviewCount, fetchedReviewCount)
    }
}
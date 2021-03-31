package dev.fabiou.appvox.core

import dev.fabiou.appvox.core.appstore.review.repository.ItunesRssReviewRepository
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlinx.coroutines.flow.take

class AppStoreTest : BaseRepositoryTest() {

    private var appStore = AppStore(RequestConfiguration(requestDelay = 3000))

//    @ParameterizedTest
//    @CsvSource(
//        "333903271, us, 10"
//    )
//    fun `Get most relevant App Store reviews using iterator`(
//        appId: String,
//        region: String,
//        requestedReviewCount: Int) {
//
//        stubHttpUrl(AppStoreReviewRepository.APP_HP_URL_PATH.format(region, appId), "mock-bearer-token")
//        val mockData = javaClass.getResource("/appstore_reviews_mock_data.json").readText()
//        stubHttpUrl(AppStoreReviewRepository.REQUEST_URL_PATH.format(region, appId, AppStoreReviewRepository.REQUEST_REVIEW_SIZE), mockData)
//
//        val request = AppStoreReviewRequest(
//            appId = appId,
//            region = region,
//            sortType = AppStoreSortType.RELEVANT,
//            maxCount = requestedReviewCount)
//
//        var fetchedReviewCount = 0
//        appStore.reviews(request)
//            .forEach { _ ->
//                fetchedReviewCount++
//            }
//
//        Assertions.assertEquals(requestedReviewCount, fetchedReviewCount)
//    }

//    @ParameterizedTest
//    @CsvSource(
//        "333903271, us, 1, 50"
//    )
//    fun `Get most recent App Store reviews using iterator`(
//        appId: String,
//        region: String,
//        pageNo: Int,
//        requestedReviewCount: Int) {
//
//        val mockData = javaClass.getResource("/appstore_recent_reviews_rss_mock_data.xml").readText()
//        stubHttpUrl(ItunesRssReviewRepository.RSS_REQUEST_URL_PATH.format(region, pageNo, appId), mockData)
//
//        val request = AppReviewRequest(
//            appId = appId,
//            region = region,
//            sortType = AppReviewSortType.RECENT,
//            maxCount = requestedReviewCount)
//
//        var fetchedReviewCount = 0
//        appVox.reviews(request)
//            .forEach { _ ->
//                fetchedReviewCount++
//            }
//
//        Assertions.assertEquals(requestedReviewCount, fetchedReviewCount)
//    }

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 1, 50"
    )
    fun `Get most recent App Store reviews using iterator`(
        appId: String,
        region: String,
        pageNo: Int,
        maxCount: Int) = runBlocking {

        val mockData = javaClass.getResource("/appstore_recent_reviews_rss_mock_data.xml").readText()
        stubHttpUrl(ItunesRssReviewRepository.RSS_REQUEST_URL_PATH.format(region, pageNo, appId), mockData)

        var fetchedReviewCount = 0
        appStore.reviews(appId)
            .take(maxCount)
            .collect { review ->
                println(review.comment)
                fetchedReviewCount++
        }

        Assertions.assertEquals(maxCount, fetchedReviewCount)
    }
}
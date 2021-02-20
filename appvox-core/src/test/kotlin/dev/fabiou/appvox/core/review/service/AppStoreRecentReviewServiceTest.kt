package dev.fabiou.appvox.core.review.service

import dev.fabiou.appvox.core.BaseStoreServiceTest
import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.constant.AppStoreSortType
import dev.fabiou.appvox.core.review.domain.request.AppStoreReviewRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreRecentReviewServiceTest : BaseStoreServiceTest() {

    private var service = AppStoreRecentReviewService(Configuration(requestDelay = 3000L))

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 50, 1"
    )
    fun `Get most recent App Store reviews from itunes RSS Feed`(appId: String, region: String, requestedSize: Int, pageNo: Int) {

        val mockData = javaClass.getResource("/appstore_recent_reviews_rss_mock_data.xml").readText()
        stubHttpUrl(AppStoreRecentReviewService.RSS_REQUEST_URL_PATH.format(region, pageNo, appId), mockData)

        val request = AppStoreReviewRequest(
            region = region,
            sortType = AppStoreSortType.RECENT,
            pageNo = 1
        )

        val response = service.getReviewsByAppId(appId, request)

        Assertions.assertEquals(requestedSize, response.entry?.size)
    }
}
package dev.fabiou.appvox.core.review.service

import dev.fabiou.appvox.core.BaseStoreServiceTest
import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.constant.AppStoreSortType
import dev.fabiou.appvox.core.review.domain.request.AppStoreReviewRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreReviewServiceTest : BaseStoreServiceTest() {

    private var service = AppStoreReviewService(Configuration(requestDelay = 3000L))

    @ParameterizedTest
    @CsvSource(
            "333903271, us, 10"
    )
    fun `Get app store reviews`(
            appId: String, region: String, requestedSize: Int) {

        stubHttpUrl(AppStoreReviewService.APP_HP_URL_PATH.format(region, appId), "mock-bearer-token")
        val mockData = javaClass.getResource("/appstore_reviews_mock_data.json").readText()
        stubHttpUrl(AppStoreReviewService.REQUEST_URL_PATH.format(region, appId, AppStoreReviewService.REQUEST_REVIEW_SIZE), mockData)

        val bearerToken = service.getBearerToken(
                appId = appId,
                region = region
        )

        val request = AppStoreReviewRequest(
                region = region,
                sortType = AppStoreSortType.RELEVANT,
                bearerToken = bearerToken
        )

        val response = service.getReviewsByAppId(appId, request)

        Assertions.assertNotNull(response.data)
        Assertions.assertEquals(requestedSize, response.data.size)
    }


}
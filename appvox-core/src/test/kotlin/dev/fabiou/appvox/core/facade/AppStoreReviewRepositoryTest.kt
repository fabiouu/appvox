package dev.fabiou.appvox.core.facade

import dev.fabiou.appvox.core.BaseRepositoryTest
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.appstore.review.AppStoreSortType
import dev.fabiou.appvox.core.appstore.review.AppStoreReviewRequest
import dev.fabiou.appvox.core.appstore.review.AppStoreReviewRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreReviewRepositoryTest : BaseRepositoryTest() {

    private var service = AppStoreReviewRepository(RequestConfiguration(requestDelay = 3000L))

    @ParameterizedTest
    @CsvSource(
            "333903271, us, 10"
    )
    fun `Get app store reviews`(
            appId: String, region: String, requestedSize: Int) {

        stubHttpUrl(AppStoreReviewRepository.APP_HP_URL_PATH.format(region, appId), "mock-bearer-token")
        val mockData = javaClass.getResource("/appstore_reviews_mock_data.json").readText()
        stubHttpUrl(
            AppStoreReviewRepository.REQUEST_URL_PATH.format(region, appId,
                AppStoreReviewRepository.REQUEST_REVIEW_SIZE
            ), mockData)

        val bearerToken = service.getBearerToken(
                appId = appId,
                region = region
        )

        val request = AppStoreReviewRequest(
                appId  = appId,
                region = region,
                sortType = AppStoreSortType.RELEVANT,
                bearerToken = bearerToken
        )

        val response = service.getReviewsByAppId(request)

        Assertions.assertNotNull(response.data)
        Assertions.assertEquals(requestedSize, response.data.size)
    }


}
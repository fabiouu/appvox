package dev.fabiou.appvox.core.review.appstore

import dev.fabiou.appvox.core.BaseNetworkTest
import dev.fabiou.appvox.core.app.appstore.AppStoreRepository
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.review.ReviewRequest
import dev.fabiou.appvox.core.review.appstore.domain.AppStoreReviewRequest
import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreRegion
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreReviewNetworkTest : BaseNetworkTest() {

    private var appStoreRepository = AppStoreRepository(RequestConfiguration(delay = 3000))

    private val repository = AppStoreReviewRepository(RequestConfiguration(delay = 3000))

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
            "333903271, us, 10"
    )
    fun `Get app store reviews`(appId: String, regionCode: String, requestedSize: Int) {
        AppStoreReviewRepository.REQUEST_URL_DOMAIN =
                UrlUtil.getUrlDomainByEnv(AppStoreReviewRepository.REQUEST_URL_DOMAIN)
        AppStoreRepository.APP_HP_URL_DOMAIN =
                UrlUtil.getUrlDomainByEnv(AppStoreRepository.APP_HP_URL_DOMAIN)
        val region = AppStoreRegion.fromValue(regionCode)
        stubHttpUrl(AppStoreRepository.APP_HP_URL_PATH.format(region.code, appId), "mock-bearer-token")
        val mockData = javaClass.getResource("/review/app_store/appstore_reviews_mock_data.json").readText()
        stubHttpUrl(
                AppStoreReviewRepository.REQUEST_URL_PATH.format(
                        region.code, appId,
                        AppStoreReviewRepository.REQUEST_REVIEW_SIZE
                ), mockData
        )

        val bearerToken = appStoreRepository.getBearerToken(
                appId = appId,
                region = region
        )

        val request = AppStoreReviewRequest(
                appId = appId,
                region = region,
                bearerToken = bearerToken
        )

        val response = repository.getReviewsByAppId(ReviewRequest((request)))

        Assertions.assertNotNull(response.results)
        Assertions.assertEquals(requestedSize, response.results.size)
    }
}

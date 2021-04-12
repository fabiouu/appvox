package dev.fabiou.appvox.core.review.appstore

import dev.fabiou.appvox.core.BaseRepositoryTest
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.review.ReviewRequest
import dev.fabiou.appvox.core.review.appstore.domain.AppStoreReviewRequest
import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreRegion
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreReviewRepositoryTest : BaseRepositoryTest() {

    private val repository = AppStoreReviewRepository(RequestConfiguration(delay = 3000))

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "333903271, us, 10"
    )
    fun `Get app store reviews`(appId: String, regionCode: String, requestedSize: Int) {
        AppStoreReviewRepository.REQUEST_URL_DOMAIN =
                UrlUtil.getUrlDomainByEnv(AppStoreReviewRepository.REQUEST_URL_DOMAIN)
        val region = AppStoreRegion.fromValue(regionCode)
        val mockData = javaClass.getResource("/review/appstore_reviews_mock_data.json").readText()
        stubHttpUrl(
            AppStoreReviewRepository.REQUEST_URL_PATH.format(
                region.code, appId,
                AppStoreReviewRepository.REQUEST_REVIEW_SIZE
            ), mockData
        )

        val request = AppStoreReviewRequest(
            appId = appId,
            region = region,
            bearerToken = "mock-bearer-token"
        )

        val response = repository.getReviewsByAppId(ReviewRequest((request)))

        Assertions.assertNotNull(response.results)
        Assertions.assertEquals(requestedSize, response.results.size)
    }
}

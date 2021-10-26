package io.appvox.appstore.review

import io.appvox.appstore.BaseAppStoreMockTest
import io.appvox.appstore.app.AppStoreRepository
import io.appvox.appstore.app.AppStoreRepository.Companion.APP_HP_URL_DOMAIN
import io.appvox.appstore.app.AppStoreRepository.Companion.APP_HP_URL_PATH
import io.appvox.appstore.review.AppStoreReviewRepository.Companion.REQUEST_REVIEW_SIZE
import io.appvox.appstore.review.AppStoreReviewRepository.Companion.REQUEST_URL_DOMAIN
import io.appvox.appstore.review.AppStoreReviewRepository.Companion.REQUEST_URL_PATH
import io.appvox.appstore.review.constant.AppStoreRegion
import io.appvox.appstore.review.domain.AppStoreReviewRequestParameters
import io.appvox.core.configuration.RequestConfiguration
import io.appvox.core.review.ReviewRequest
import io.kotest.assertions.assertSoftly
import io.kotest.inspectors.forExactly
import io.kotest.matchers.ints.shouldBeBetween
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContainOnlyDigits
import io.kotest.matchers.string.shouldNotBeEmpty
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreReviewRepositoryTest : BaseAppStoreMockTest() {

    private var appStoreRepository = AppStoreRepository(RequestConfiguration(delay = 3000))

    private val repository = AppStoreReviewRepository(RequestConfiguration(delay = 3000))

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "333903271, us, 10"
    )
    fun `get app store reviews`(
        appId: String,
        regionCode: String,
        expectedReviewCount: Int
    ) {
        REQUEST_URL_DOMAIN = httpMockServerDomain
        APP_HP_URL_DOMAIN = httpMockServerDomain

        val region = AppStoreRegion.fromValue(regionCode)
        stubHttpUrl(APP_HP_URL_PATH.format(region.code, appId), "mock-bearer-token")

        val mockData = javaClass.getResource("/review/appstore/appstore_reviews_mock_data.json").readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region.code, appId, REQUEST_REVIEW_SIZE), mockData)

        val bearerToken = appStoreRepository.getBearerToken(
            appId = appId,
            region = region
        )

        val request = AppStoreReviewRequestParameters(
            appId = appId,
            region = region,
            bearerToken = bearerToken
        )

        val response = repository.getReviewsByAppId(ReviewRequest(request))

        response.results?.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id.shouldNotBeEmpty()
                id.shouldContainOnlyDigits()
                type shouldBe "user-reviews"
                attributes.shouldNotBeNull()
                attributes.userName.shouldNotBeEmpty()
                attributes.review.shouldNotBeEmpty()
                attributes.title.shouldNotBeEmpty()
                attributes.rating.shouldBeBetween(1, 5)
                attributes.developerResponse?.let { developerResponse ->
                    developerResponse.body.shouldNotBeEmpty()
                }
            }
        }
        response.nextToken.shouldNotBeEmpty()
    }
}

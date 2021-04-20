package dev.fabiou.appvox.review.appstore

import dev.fabiou.appvox.BaseMockTest
import dev.fabiou.appvox.app.appstore.AppStoreRepository
import dev.fabiou.appvox.app.appstore.AppStoreRepository.Companion.APP_HP_URL_DOMAIN
import dev.fabiou.appvox.app.appstore.AppStoreRepository.Companion.APP_HP_URL_PATH
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.ReviewRequest
import dev.fabiou.appvox.review.appstore.AppStoreReviewRepository.Companion.REQUEST_REVIEW_SIZE
import dev.fabiou.appvox.review.appstore.AppStoreReviewRepository.Companion.REQUEST_URL_DOMAIN
import dev.fabiou.appvox.review.appstore.AppStoreReviewRepository.Companion.REQUEST_URL_PATH
import dev.fabiou.appvox.review.appstore.domain.AppStoreReviewRequest
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion
import io.kotest.assertions.assertSoftly
import io.kotest.inspectors.forExactly
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeBetween
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeBetween
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldContainOnlyDigits
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreReviewRepositoryTest : BaseMockTest() {

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
        REQUEST_URL_DOMAIN = UrlUtil.getUrlDomainByEnv(REQUEST_URL_DOMAIN)
        APP_HP_URL_DOMAIN = UrlUtil.getUrlDomainByEnv(APP_HP_URL_DOMAIN)

        val region = AppStoreRegion.fromValue(regionCode)
        stubHttpUrl(APP_HP_URL_PATH.format(region.code, appId), "mock-bearer-token")

        val mockData = javaClass.getResource("/review/app_store/appstore_reviews_mock_data.json").readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region.code, appId, REQUEST_REVIEW_SIZE), mockData)

        val bearerToken = appStoreRepository.getBearerToken(
            appId = appId,
            region = region
        )

        val request = AppStoreReviewRequest(
            appId = appId,
            region = region,
            bearerToken = bearerToken
        )

        val response = repository.getReviewsByAppId(ReviewRequest(request))

        response.results.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id.shouldNotBeEmpty()
                id.shouldContainOnlyDigits()
                type shouldBe "user-reviews"
                attributes.shouldNotBeNull()
                attributes.userName.shouldNotBeEmpty()
                attributes.review.shouldNotBeEmpty()
                // TODO attributes.date
                attributes.title.shouldNotBeEmpty()
                attributes.rating.shouldBeBetween(1, 5)
                attributes.developerResponse?.let { developerResponse ->
                    developerResponse.body.shouldNotBeEmpty()
//                    developerResponse.modified
                }
            }
        }
        response.nextToken.shouldNotBeEmpty()
    }
}

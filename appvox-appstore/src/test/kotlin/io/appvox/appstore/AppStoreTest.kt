package io.appvox.appstore

import io.appvox.appstore.app.AppStoreRepository.Companion.APP_HP_URL_DOMAIN
import io.appvox.appstore.app.AppStoreRepository.Companion.APP_HP_URL_PATH
import io.appvox.appstore.review.AppStoreReviewRepository
import io.appvox.appstore.review.AppStoreReviewRepository.Companion.REQUEST_URL_DOMAIN
import io.appvox.appstore.review.AppStoreReviewRepository.Companion.REQUEST_URL_PATH
import io.appvox.appstore.review.constant.AppStoreRegion.Companion.fromValue
import io.appvox.appstore.review.domain.AppStoreReview
import io.appvox.core.configuration.RequestConfiguration
import io.kotest.assertions.assertSoftly
import io.kotest.inspectors.forExactly
import io.kotest.matchers.ints.shouldBeBetween
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeEmpty
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreTest : BaseAppStoreMockTest() {

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "333903271, us, 10"
    )
    fun `Get App Store reviews using default optional parameters`(
        appId: String,
        regionCode: String,
        expectedReviewCount: Int
    ) = runBlockingTest {
        REQUEST_URL_DOMAIN = httpMockServerDomain
        APP_HP_URL_DOMAIN = httpMockServerDomain

        val region = fromValue(regionCode)
        val bearerTokenRequestUrlPath = APP_HP_URL_PATH.format(region.code, appId)
        stubHttpUrl(bearerTokenRequestUrlPath, "mock-bearer-token")

        val mockData = javaClass.getResource("/review/appstore/appstore_reviews_mock_data.json").readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region.code, appId, AppStoreReviewRepository.REQUEST_REVIEW_SIZE), mockData)

        val reviews = ArrayList<AppStoreReview>()
        AppStore().reviews{ this.appId = appId }
            .take(expectedReviewCount)
            .collect { review ->
                reviews.add(review)
            }

        reviews.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id.shouldNotBeEmpty()
                region.shouldNotBeNull()
                userName.shouldNotBeEmpty()
                rating.shouldBeBetween(1, 5)
                title.shouldNotBeEmpty()
                comment.shouldNotBeEmpty()
                commentTime.shouldNotBeNull()
                replyComment?.let { it.shouldNotBeEmpty() }
                replyTime?.let { it.shouldNotBeNull() }
                commentTypes.shouldNotBeNull()
                userTypes.shouldNotBeNull()
            }
        }
    }

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "333903271, us, 10"
    )
    fun `get most recent App Store reviews with a delay of 3s between each request`(
        appId: String,
        regionCode: String,
        expectedReviewCount: Int
    ) = runBlockingTest {
        REQUEST_URL_DOMAIN = httpMockServerDomain
        APP_HP_URL_DOMAIN = httpMockServerDomain

        val region = fromValue(regionCode)
        val bearerTokenRequestUrlPath = APP_HP_URL_PATH.format(region.code, appId)
        stubHttpUrl(bearerTokenRequestUrlPath, "mock-bearer-token")

        val mockData = javaClass.getResource("/review/appstore/appstore_reviews_mock_data.json").readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region.code, appId, AppStoreReviewRepository.REQUEST_REVIEW_SIZE), mockData)

        val reviews = arrayListOf<AppStoreReview>()
        val appStore = AppStore(RequestConfiguration(delay = 3000))
        appStore.reviews {
            this.appId = appId
            this.region = region
        }
            .take(expectedReviewCount)
            .collect { review ->
                reviews.add(review)
            }

        reviews.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id.shouldNotBeEmpty()
                region.shouldNotBeNull()
                userName.shouldNotBeEmpty()
                rating.shouldBeBetween(1, 5)
                title.shouldNotBeEmpty()
                comment.shouldNotBeEmpty()
                commentTime.shouldNotBeNull()
                replyComment?.let { it.shouldNotBeEmpty() }
                replyTime?.let { it.shouldNotBeNull() }
                commentTypes.shouldNotBeNull()
                userTypes.shouldNotBeNull()
            }
        }
    }
}

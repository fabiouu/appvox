package dev.fabiou.appvox

import dev.fabiou.appvox.app.appstore.AppStoreRepository.Companion.APP_HP_URL_DOMAIN
import dev.fabiou.appvox.app.appstore.AppStoreRepository.Companion.APP_HP_URL_PATH
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.configuration.RequestConfiguration.Proxy
import dev.fabiou.appvox.review.appstore.AppStoreReviewRepository
import dev.fabiou.appvox.review.appstore.AppStoreReviewRepository.Companion.REQUEST_URL_DOMAIN
import dev.fabiou.appvox.review.appstore.AppStoreReviewRepository.Companion.REQUEST_URL_PATH
import dev.fabiou.appvox.review.appstore.domain.AppStoreReview
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion.Companion.fromValue
import io.kotest.assertions.assertSoftly
import io.kotest.inspectors.forExactly
import io.kotest.matchers.ints.shouldBeBetween
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeEmpty
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreTest : BaseMockTest() {

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

        val mockData = javaClass.getResource("/review/app_store/appstore_reviews_mock_data.json").readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region.code, appId, AppStoreReviewRepository.REQUEST_REVIEW_SIZE), mockData)

        val reviews = ArrayList<AppStoreReview>()
        AppStore().reviews(appId)
            .take(expectedReviewCount)
            .collect { review ->
                reviews.add(review)
            }

        reviews.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id.shouldNotBeEmpty()
                userName.shouldNotBeEmpty()
                rating.shouldBeBetween(1, 5)
                title.shouldNotBeEmpty()
                comment.shouldNotBeEmpty()
                translatedComment?.let { it.shouldNotBeEmpty() }
                commentTime.shouldNotBeNull()
                replyComment?.let { it.shouldNotBeEmpty() }
                replyTime?.let { it.shouldNotBeNull() }
                //   TODO url.shouldNotBeEmpty()
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

        val mockData = javaClass.getResource("/review/app_store/appstore_reviews_mock_data.json").readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region.code, appId, AppStoreReviewRepository.REQUEST_REVIEW_SIZE), mockData)

        val reviews = arrayListOf<AppStoreReview>()
        val appStore = AppStore(RequestConfiguration(delay = 3000))
        appStore.reviews(
            appId = appId,
            region = region
        )
            .take(expectedReviewCount)
            .collect { review ->
                reviews.add(review)
            }

        reviews.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id.shouldNotBeEmpty()
                userName.shouldNotBeEmpty()
                rating.shouldBeBetween(1, 5)
                title.shouldNotBeEmpty()
                comment.shouldNotBeEmpty()
                translatedComment?.let { it.shouldNotBeEmpty() }
                commentTime.shouldNotBeNull()
                replyComment?.let { it.shouldNotBeEmpty() }
                replyTime?.let { it.shouldNotBeNull() }
                // TODO url.shouldNotBeEmpty()
            }
        }
    }
}

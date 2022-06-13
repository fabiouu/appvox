package io.appvox.appstore

import io.appvox.appstore.review.AppStoreReviewRepository.Companion.REQUEST_URL_DOMAIN
import io.appvox.appstore.review.AppStoreReviewRepository.Companion.REQUEST_URL_PATH
import io.appvox.appstore.review.constant.AppStoreRegion.Companion.fromValue
import io.appvox.appstore.review.constant.AppStoreSortType
import io.appvox.appstore.review.domain.AppStoreReview
import io.appvox.core.configuration.RequestConfiguration
import io.kotest.assertions.assertSoftly
import io.kotest.inspectors.forExactly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.ints.shouldBeBetween
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeEmpty
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreTest : BaseAppStoreMockTest() {

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "333903271, US, 1, 50"
    )
    fun `Get reviews using default parameters`(
        appId: String,
        region: String,
        pageNo: Int,
        expectedReviewCount: Int
    ) = runTest {
        REQUEST_URL_DOMAIN = httpMockServerDomain
        val mockData = javaClass.getResource("/review/itunesrss/itunes_rss_reviews_mock_data.xml")!!.readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region, pageNo, appId), mockData)

        val reviews = ArrayList<AppStoreReview>()
        AppStore()
            .reviews { this.appId = appId }
            .take(expectedReviewCount)
            .collect { review ->
                reviews.add(review)
            }

        reviews.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id.shouldNotBeEmpty()
                region.shouldNotBeNull()
                url.shouldNotBeEmpty()
                username.shouldNotBeEmpty()
                rating.shouldBeBetween(1, 5)
                appVersion.shouldNotBeEmpty()
                title.shouldNotBeEmpty()
                comment.shouldNotBeEmpty()
                commentTime.shouldNotBeNull()
                likeCount.shouldBeGreaterThanOrEqual(0)
                commentTypes.shouldNotBeNull()
                userTypes.shouldNotBeNull()
            }
        }
    }

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "333903271, US, 1, 50"
    )
    fun `get most recent reviews with a delay of 3s between each request`(
        appId: String,
        region: String,
        pageNo: Int,
        expectedReviewCount: Int
    ) = runTest {
        REQUEST_URL_DOMAIN = httpMockServerDomain
        val mockData = javaClass.getResource("/review/itunesrss/itunes_rss_reviews_mock_data.xml")!!.readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region, pageNo, appId), mockData)

        val reviews = arrayListOf<AppStoreReview>()
        val itunesRss = AppStore(RequestConfiguration(delay = 3000))
        itunesRss.reviews {
            this.appId = appId
            this.region = fromValue(region)
            this.sortType = AppStoreSortType.RECENT
        }
            .take(expectedReviewCount)
            .collect { review ->
                reviews.add(review)
            }

        reviews.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id.shouldNotBeEmpty()
                region.shouldNotBeNull()
                url.shouldNotBeEmpty()
                username.shouldNotBeEmpty()
                rating.shouldBeBetween(1, 5)
                appVersion.shouldNotBeEmpty()
                title.shouldNotBeEmpty()
                comment.shouldNotBeEmpty()
                commentTime.shouldNotBeNull()
                likeCount.shouldBeGreaterThanOrEqual(0)
                commentTypes.shouldNotBeNull()
                userTypes.shouldNotBeNull()
            }
        }
    }

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "1208224953, ZW, 1"
    )
    fun `Get reviews with an empty response`(
        appId: String,
        region: String,
        pageNo: Int
    ) = runTest {
        REQUEST_URL_DOMAIN = httpMockServerDomain
        val mockData = javaClass.getResource("/review/itunesrss/itunes_rss_reviews_empty_mock_data.xml")!!.readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region, pageNo, appId), mockData)

        val reviews = ArrayList<AppStoreReview>()
        AppStore()
            .reviews {
                this.appId = appId
                this.region = fromValue(region)
                this.sortType = AppStoreSortType.RECENT
            }
            .collect { review ->
                reviews.add(review)
            }

        reviews.shouldBeEmpty()
    }
}

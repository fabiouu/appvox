package io.appvox.appstore

import io.appvox.appstore.review.ItunesRssReviewRepository.Companion.REQUEST_URL_DOMAIN
import io.appvox.appstore.review.ItunesRssReviewRepository.Companion.REQUEST_URL_PATH
import io.appvox.appstore.review.constant.AppStoreRegion.Companion.fromValue
import io.appvox.appstore.review.constant.ItunesRssSortType
import io.appvox.appstore.review.domain.ItunesRssReview
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ItunesRssTest : BaseAppStoreMockTest() {

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
    ) = runBlockingTest {
        REQUEST_URL_DOMAIN = httpMockServerDomain
        val mockData = javaClass.getResource("/review/itunesrss/itunes_rss_reviews_mock_data.xml").readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region, pageNo, appId), mockData)

        val reviews = ArrayList<ItunesRssReview>()
        ItunesRss()
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
                latestComment.shouldNotBeNull()
            }
            assertSoftly(result.latestUserComment) {
                userName.shouldNotBeEmpty()
                rating.shouldBeBetween(1, 5)
                appVersion.shouldNotBeEmpty()
                title.shouldNotBeEmpty()
                text.shouldNotBeEmpty()
                time.shouldNotBeNull()
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
    ) = runBlockingTest {
        REQUEST_URL_DOMAIN = httpMockServerDomain
        val mockData = javaClass.getResource("/review/itunesrss/itunes_rss_reviews_mock_data.xml").readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region, pageNo, appId), mockData)

        val reviews = arrayListOf<ItunesRssReview>()
        val itunesRss = ItunesRss(RequestConfiguration(delay = 3000))
        itunesRss.reviews {
            this.appId = appId
            this.region = fromValue(region)
            this.sortType = ItunesRssSortType.RECENT
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
                latestComment.shouldNotBeNull()
            }
            assertSoftly(result.latestUserComment) {
                userName.shouldNotBeEmpty()
                rating.shouldBeBetween(1, 5)
                appVersion.shouldNotBeEmpty()
                title.shouldNotBeEmpty()
                text.shouldNotBeEmpty()
                time.shouldNotBeNull()
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
    ) = runBlockingTest {
        REQUEST_URL_DOMAIN = httpMockServerDomain
        val mockData = javaClass.getResource("/review/itunesrss/itunes_rss_reviews_empty_mock_data.xml").readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region, pageNo, appId), mockData)

        val reviews = ArrayList<ItunesRssReview>()
        ItunesRss()
            .reviews {
                this.appId = appId
                this.region = fromValue(region)
                this.sortType = ItunesRssSortType.RECENT
            }
            .collect { review ->
                reviews.add(review)
            }

        reviews.shouldBeEmpty()
    }
}

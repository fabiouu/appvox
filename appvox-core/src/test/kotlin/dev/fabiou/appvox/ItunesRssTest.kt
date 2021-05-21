package dev.fabiou.appvox

import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.itunesrss.ItunesRssReviewRepository.Companion.REQUEST_URL_DOMAIN
import dev.fabiou.appvox.review.itunesrss.ItunesRssReviewRepository.Companion.REQUEST_URL_PATH
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion.Companion.fromValue
import dev.fabiou.appvox.review.itunesrss.constant.ItunesRssSortType
import dev.fabiou.appvox.review.itunesrss.domain.ItunesRssReview
import io.kotest.assertions.assertSoftly
import io.kotest.inspectors.forExactly
import io.kotest.matchers.ints.shouldBeBetween
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeEmpty
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ItunesRssTest : BaseMockTest() {

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "333903271, us, 1, 50"
    )
    fun `Get iTunes RSS Feed reviews using default optional parameters`(
        appId: String,
        region: String,
        pageNo: Int,
        expectedReviewCount: Int
    ) = runBlockingTest {
        REQUEST_URL_DOMAIN = httpMockServerDomain
        val mockData = javaClass.getResource("/review/itunes_rss/itunes_rss_reviews_mock_data.xml").readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region, pageNo, appId), mockData)

        val reviews = ArrayList<ItunesRssReview>()
        ItunesRss()
            .reviews(appId)
            .take(expectedReviewCount)
            .collect { review ->
                reviews.add(review)
            }

        reviews.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id.shouldNotBeEmpty()
                url.shouldNotBeEmpty()
            }
            assertSoftly(result.latestUserComment) {
                userName.shouldNotBeEmpty()
                rating.shouldBeBetween(1, 5)
                appVersion.shouldNotBeEmpty()
                title.shouldNotBeEmpty()
                text.shouldNotBeEmpty()
                time.shouldNotBeNull()
                likeCount?.shouldBeGreaterThanOrEqual(0)
            }
        }
    }

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "333903271, us, 1, 50"
    )
    fun `get most recent iTunes RSS Feed reviews with a delay of 3s between each request`(
        appId: String,
        region: String,
        pageNo: Int,
        expectedReviewCount: Int
    ) = runBlockingTest {
        REQUEST_URL_DOMAIN = httpMockServerDomain
        val mockData = javaClass.getResource("/review/itunes_rss/itunes_rss_reviews_mock_data.xml").readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region, pageNo, appId), mockData)

        val reviews = arrayListOf<ItunesRssReview>()
        val appStore = ItunesRss(RequestConfiguration(delay = 3000))
        appStore.reviews(
            appId = appId,
            region = fromValue(region),
            sortType = ItunesRssSortType.RECENT
        )
            .take(expectedReviewCount)
            .collect { review ->
                reviews.add(review)
            }

        reviews.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id.shouldNotBeEmpty()
                url.shouldNotBeEmpty()
            }
            assertSoftly(result.latestUserComment) {
                userName.shouldNotBeEmpty()
                rating.shouldBeBetween(1, 5)
                appVersion.shouldNotBeEmpty()
                title.shouldNotBeEmpty()
                text.shouldNotBeEmpty()
                time.shouldNotBeNull()
                likeCount?.shouldBeGreaterThanOrEqual(0)
            }
        }
    }
}

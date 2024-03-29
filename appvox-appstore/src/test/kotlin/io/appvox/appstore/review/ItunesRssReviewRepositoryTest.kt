package io.appvox.appstore.review

import io.appvox.appstore.BaseAppStoreMockTest
import io.appvox.appstore.review.AppStoreReviewRepository.Companion.REQUEST_URL_DOMAIN
import io.appvox.appstore.review.AppStoreReviewRepository.Companion.REQUEST_URL_PATH
import io.appvox.appstore.review.constant.AppStoreRegion
import io.appvox.appstore.review.constant.ItunesRssSortType
import io.appvox.appstore.review.domain.ItunesRssReviewRequestParameters
import io.appvox.configuration.RequestConfiguration
import io.appvox.review.ReviewRequest
import io.kotest.assertions.assertSoftly
import io.kotest.inspectors.forExactly
import io.kotest.matchers.string.shouldContainOnlyDigits
import io.kotest.matchers.string.shouldNotBeEmpty
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ItunesRssReviewRepositoryTest : BaseAppStoreMockTest() {

    private val repository = ItunesRssReviewRepository(RequestConfiguration(delay = 3000))

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "333903271, us, 50, 1"
    )
    fun `Get most recent App Store reviews from itunes RSS Feed`(
        appId: String,
        regionCode: String,
        expectedReviewCount: Int,
        pageNo: Int
    ) {
        REQUEST_URL_DOMAIN = httpMockServerDomain
        val region = AppStoreRegion.fromValue(regionCode)
        val mockData = javaClass.getResource("/review/itunesrss/itunes_rss_reviews_mock_data.xml").readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region.code, pageNo, appId), mockData)

        val request = ItunesRssReviewRequestParameters(
            appId = appId,
            region = region,
            sortType = ItunesRssSortType.RECENT,
            pageNo = 1
        )

        val response = repository.getReviewsByAppId(ReviewRequest(request))

        response.results.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id.shouldNotBeEmpty()
                id.shouldContainOnlyDigits()
                title.shouldNotBeEmpty()
            }
        }
        response.nextToken.shouldNotBeEmpty()
    }
}

package io.appvox.appstore.review

import io.appvox.appstore.BaseAppStoreMockTest
import io.appvox.appstore.review.AppStoreReviewRepository.Companion.REQUEST_URL_DOMAIN
import io.appvox.appstore.review.AppStoreReviewRepository.Companion.REQUEST_URL_PATH
import io.appvox.appstore.review.constant.AppStoreRegion
import io.appvox.appstore.review.constant.AppStoreSortType
import io.appvox.appstore.review.domain.AppStoreReviewRequestParameters
import io.appvox.core.configuration.RequestConfiguration
import io.appvox.core.review.ReviewRequest
import io.kotest.assertions.assertSoftly
import io.kotest.inspectors.forExactly
import io.kotest.matchers.string.shouldContainOnlyDigits
import io.kotest.matchers.string.shouldNotBeEmpty
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreReviewRepositoryTest : BaseAppStoreMockTest() {

    private val repository = AppStoreReviewRepository(RequestConfiguration(delay = 3000))

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "333903271, US, 50, 1"
    )
    fun `Get most recent App Store reviews from itunes RSS Feed`(
        appId: String,
        regionCode: String,
        expectedReviewCount: Int,
        pageNo: Int
    ) {
        REQUEST_URL_DOMAIN = httpMockServerDomain
        val region = AppStoreRegion.fromValue(regionCode)
        val mockData = javaClass.getResource("/review/itunesrss/itunes_rss_reviews_mock_data.xml")!!.readText()
        stubHttpUrl(REQUEST_URL_PATH.format(region.code, pageNo, appId, AppStoreSortType.RECENT.value), mockData)

        val request = AppStoreReviewRequestParameters(
            appId = appId,
            region = region,
            sortType = AppStoreSortType.RECENT,
            pageNo = pageNo
        )

        val response = repository.getReviewsByAppId(ReviewRequest(request))

        response.results?.forExactly(expectedReviewCount) { result ->
            assertSoftly(result) {
                id.shouldNotBeEmpty()
                id.shouldContainOnlyDigits()
                title.shouldNotBeEmpty()
            }
        }
        response.nextToken.shouldNotBeEmpty()
    }
}

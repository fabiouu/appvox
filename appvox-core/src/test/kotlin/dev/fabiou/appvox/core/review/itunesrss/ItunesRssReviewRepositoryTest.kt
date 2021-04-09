package dev.fabiou.appvox.core.review.itunesrss

import dev.fabiou.appvox.core.BaseRepositoryTest
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.review.ReviewRequest
import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreRegion
import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreSortType
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReviewRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ItunesRssReviewRepositoryTest : BaseRepositoryTest() {

    private val repository = ItunesRssReviewRepository(RequestConfiguration(requestDelay = 3000L))

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "333903271, us, 50, 1"
    )
    fun `Get most recent App Store reviews from itunes RSS Feed`(
        appId: String,
        regionCode: String,
        requestedSize: Int,
        pageNo: Int
    ) {

        val region = AppStoreRegion.fromValue(regionCode)

        val mockData = javaClass.getResource("/review/itunes_rss_reviews_mock_data.xml").readText()
        stubHttpUrl(ItunesRssReviewRepository.RSS_REQUEST_URL_PATH.format(region.code, pageNo, appId), mockData)

        val request = ItunesRssReviewRequest(
            appId = appId,
            region = region,
            sortType = AppStoreSortType.RECENT,
            pageNo = 1
        )

        val response = repository.getReviewsByAppId(ReviewRequest(request))

        Assertions.assertEquals(requestedSize, response.results.size)
    }
}

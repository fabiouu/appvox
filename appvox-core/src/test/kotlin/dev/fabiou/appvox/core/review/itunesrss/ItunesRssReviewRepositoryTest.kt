package dev.fabiou.appvox.core.review.itunesrss

import dev.fabiou.appvox.core.BaseRepositoryTest
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.appstore.review.domain.ItunesRssReviewRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ItunesRssReviewRepositoryTest : BaseRepositoryTest() {

    private var repository = ItunesRssReviewRepository(RequestConfiguration(requestDelay = 3000L))

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 50, 1"
    )
    fun `Get most recent App Store reviews from itunes RSS Feed`(appId: String, region: String, requestedSize: Int, pageNo: Int) {

        val mockData = javaClass.getResource("/appstore_recent_reviews_rss_mock_data.xml").readText()
        stubHttpUrl(ItunesRssReviewRepository.RSS_REQUEST_URL_PATH.format(region, pageNo, appId), mockData)

        val request = ItunesRssReviewRequest(
            appId = appId,
            region = region,
            sortType = AppStoreSortType.RECENT,
            pageNo = 1
        )

        val response = repository.getReviewsByAppId(request)

        Assertions.assertEquals(requestedSize, response.result.entry!!.size)
    }
}
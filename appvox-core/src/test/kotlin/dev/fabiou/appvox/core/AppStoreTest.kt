package dev.fabiou.appvox.core

import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.review.itunesrss.ItunesRssReviewRepository
import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreRegion
import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreSortType
import dev.fabiou.appvox.core.review.itunesrss.domain.ItunesRssReview
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreTest : BaseRepositoryTest() {

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "333903271, us, 1, 50"
    )
    fun `Get most recent iTunes RSS Feed reviews from the US with a delay of 3s between each network call`(
        appId: String,
        region: String,
        pageNo: Int,
        maxReviewCount: Int
    ) = runBlockingTest {
        ItunesRssReviewRepository.REQUEST_URL_DOMAIN =
                UrlUtil.getUrlDomainByEnv(ItunesRssReviewRepository.REQUEST_URL_DOMAIN)
        val mockData = javaClass.getResource("/review/itunes_rss/itunes_rss_reviews_mock_data.xml").readText()
        stubHttpUrl(ItunesRssReviewRepository.REQUEST_URL_PATH.format(region, pageNo, appId), mockData)

        val fetchedReviews = arrayListOf<ItunesRssReview>()
        val appStore = AppStore(RequestConfiguration(delay = 3000))
        appStore.reviews(
            appId = appId,
            region = AppStoreRegion.fromValue(region),
            sortType = AppStoreSortType.RECENT
        )
            .take(maxReviewCount)
            .collect { review ->
                fetchedReviews.add(review)
            }

        Assertions.assertEquals(maxReviewCount, fetchedReviews.size)
    }

    @ExperimentalCoroutinesApi
    @ParameterizedTest
    @CsvSource(
        "333903271, us, 1, 50"
    )
    fun `Get iTunes RSS Feed reviews using default optional parameters`(
        appId: String,
        region: String,
        pageNo: Int,
        maxReviewCount: Int
    ) = runBlockingTest {

        val mockData = javaClass.getResource("/review/itunes_rss/itunes_rss_reviews_mock_data.xml").readText()
        stubHttpUrl(ItunesRssReviewRepository.REQUEST_URL_PATH.format(region, pageNo, appId), mockData)

        var fetchedReviewCount = 0
        val appStore = AppStore()
        appStore.reviews(appId)
            .take(maxReviewCount)
            .collect { _ ->
                fetchedReviewCount++
            }

        Assertions.assertEquals(maxReviewCount, fetchedReviewCount)
    }
}

package dev.fabiou.appvox.app.appstore

import dev.fabiou.appvox.BaseMockTest
import dev.fabiou.appvox.app.appstore.AppStoreRepository.Companion.APP_HP_URL_DOMAIN
import dev.fabiou.appvox.app.appstore.AppStoreRepository.Companion.APP_HP_URL_PATH
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreRepositoryTest : BaseMockTest() {

    private val appStoreRepository = AppStoreRepository(RequestConfiguration(delay = 3000))

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 10"
    )
    fun `get App Store bearer token`(appId: String, regionCode: String) {
        APP_HP_URL_DOMAIN = httpMockServerDomain
        val region = AppStoreRegion.fromValue(regionCode)
        stubHttpUrl(APP_HP_URL_PATH.format(region.code, appId), "mock-bearer-token")

        val bearerToken = appStoreRepository.getBearerToken(
            appId = appId,
            region = region
        )

        Assertions.assertNotNull(bearerToken)
    }
}


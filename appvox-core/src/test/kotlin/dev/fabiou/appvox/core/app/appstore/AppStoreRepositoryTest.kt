package dev.fabiou.appvox.core.app.appstore

import dev.fabiou.appvox.core.BaseRepositoryTest
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.review.itunesrss.constant.AppStoreRegion
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreRepositoryTest : BaseRepositoryTest() {

    private val appStoreRepository = AppStoreRepository(RequestConfiguration(requestDelay = 3000L))

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 10"
    )
    fun `Get App Store bearer token`(appId: String, regionCode: String) {
        val region = AppStoreRegion.fromValue(regionCode)

        stubHttpUrl(AppStoreRepository.APP_HP_URL_PATH.format(region.code, appId), "mock-bearer-token")

        val bearerToken = appStoreRepository.getBearerToken(
            appId = appId,
            region = region
        )

        Assertions.assertNotNull(bearerToken)
    }
}

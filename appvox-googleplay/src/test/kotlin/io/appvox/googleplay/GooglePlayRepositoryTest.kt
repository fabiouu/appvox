package io.appvox.googleplay

import io.appvox.core.configuration.RequestConfiguration
import io.appvox.googleplay.app.GooglePlayRepository
import io.appvox.googleplay.app.GooglePlayRepository.Companion.APP_HP_URL_DOMAIN
import io.appvox.googleplay.app.GooglePlayRepository.Companion.APP_HP_URL_PATH
import io.appvox.googleplay.review.constant.GooglePlayLanguage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.time.Duration.Companion.milliseconds

class GooglePlayRepositoryTest : BaseGooglePlayMockTest() {

    private val repository = GooglePlayRepository(RequestConfiguration(delay = 3000.milliseconds))

    @ParameterizedTest
    @CsvSource(
        "com.twitter.android"
    )
    fun `Extract sid and bl request's parameters from app homepage`(appId: String) {
        APP_HP_URL_DOMAIN = httpMockServerDomain
        val mockData = javaClass.getResource("/reviews/$appId/app_homepage.html").readText()
        stubHttpUrl(APP_HP_URL_PATH, mockData)

        val scriptParameters = repository.getScriptParameters(
            appId = appId,
            language = GooglePlayLanguage.ENGLISH_US
        )

        Assertions.assertNotNull(scriptParameters["sid"])
        Assertions.assertNotNull(scriptParameters["bl"])
    }
}

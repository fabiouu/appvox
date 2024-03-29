package io.appvox.googleplay.app

import io.appvox.googleplay.BaseGooglePlayMockTest
import io.appvox.configuration.RequestConfiguration
import io.appvox.googleplay.app.GooglePlayRepository.Companion.APP_HP_URL_DOMAIN
import io.appvox.googleplay.app.GooglePlayRepository.Companion.APP_HP_URL_PATH
import io.appvox.googleplay.review.constant.GooglePlayLanguage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GooglePlayRepositoryTest : BaseGooglePlayMockTest() {

    private val googlePlayRepository = GooglePlayRepository(RequestConfiguration(delay = 3000))

    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, en-US"
    )
    fun `get Google Play request parameters`(
        appId: String,
        languageCode: String
    ) {
        APP_HP_URL_DOMAIN = httpMockServerDomain
        val mockData = javaClass.getResource(
            "/app/com.twitter.android" +
                "/app_googleplay_com.twitter.android_homepage.html"
        ).readText()

        stubHttpUrl(APP_HP_URL_PATH, mockData)
        val scriptParameters = googlePlayRepository.getScriptParameters(
            appId = appId,
            language = GooglePlayLanguage.fromValue(languageCode)
        )

        Assertions.assertNotNull(scriptParameters["sid"])
        Assertions.assertNotNull(scriptParameters["bl"])
    }
}

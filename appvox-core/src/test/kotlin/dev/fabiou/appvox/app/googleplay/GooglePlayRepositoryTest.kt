package dev.fabiou.appvox.app.googleplay

import dev.fabiou.appvox.BaseMockTest
import dev.fabiou.appvox.app.googleplay.GooglePlayRepository.Companion.APP_HP_URL_DOMAIN
import dev.fabiou.appvox.app.googleplay.GooglePlayRepository.Companion.APP_HP_URL_PATH
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.googleplay.constant.GooglePlayLanguage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GooglePlayRepositoryTest : BaseMockTest() {

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
            "/app/googleplay/com.twitter.android" +
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


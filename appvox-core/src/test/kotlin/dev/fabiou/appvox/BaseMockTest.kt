package dev.fabiou.appvox

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseMockTest {

    private val wireMockServer = WireMockServer()

    @BeforeAll
    fun initWireMockServer() {
        wireMockServer.start()
        WireMock.configureFor("localhost", 8080)
    }

    @AfterAll
    fun destroyWireMockServer() {
        wireMockServer.stop()
    }

    fun stubHttpUrl(urlPath: String, mockResponse: String) {
        WireMock.stubFor(WireMock.any(WireMock.urlPathEqualTo(urlPath))
                .willReturn(WireMock.aResponse().withBody(mockResponse)))
    }
}

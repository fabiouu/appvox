package io.appvox.googleplay

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

// TODO: Move to Kotest extension
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseGooglePlayMockTest {

    protected val httpMockServerDomain = "http://localhost:8080"

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

    fun stubHttpUrlWithStatus(urlPath: String, mockResponse: String, responseStatus: Int) {
        WireMock.stubFor(WireMock.any(WireMock.urlPathEqualTo(urlPath))
            .willReturn(WireMock.aResponse().withStatus(responseStatus).withBody(mockResponse)))
    }
}

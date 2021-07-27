package io.appvox.appstore

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseAppStoreMockTest {

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
}

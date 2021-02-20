package dev.fabiou.appvox.core

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class BaseStoreServiceTest {

    protected val wireMockServer = WireMockServer()

    @BeforeEach
    fun initWireMockServer() {
        wireMockServer.start()
        WireMock.configureFor("localhost", 8080)
    }

    @AfterEach
    fun destroyWireMockServer() {
        wireMockServer.stop()
    }

    fun stubHttpUrl(urlPath: String, mockResponse: String) {
        WireMock.stubFor(WireMock.any(WireMock.urlPathEqualTo(urlPath))
                .willReturn(WireMock.aResponse().withBody(mockResponse)))
    }

}
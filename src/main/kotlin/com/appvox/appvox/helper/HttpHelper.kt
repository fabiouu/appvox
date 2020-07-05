package com.appvox.appvox.helper

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.net.InetSocketAddress
import java.net.Proxy

@Configuration
@Component
class HttpHelper(

    @Value("\${scraper.proxy.host:}")
    private val proxyHost: String,

    @Value("\${scraper.proxy.port:#{0}}")
    private val proxyPort : Int
)  {

    fun getRestTemplate() : RestTemplate {
        val requestFactory = SimpleClientHttpRequestFactory()
        if (proxyHost.isNotEmpty() && proxyPort != 0) {
            val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(proxyHost, proxyPort))
            requestFactory.setProxy(proxy)
        }
        return RestTemplate(requestFactory);
    }
}
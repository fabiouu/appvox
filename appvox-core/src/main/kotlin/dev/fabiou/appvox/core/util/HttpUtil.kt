package dev.fabiou.appvox.core.util

import dev.fabiou.appvox.core.configuration.ProxyConfiguration
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.Proxy.NO_PROXY
import java.net.Proxy.Type.HTTP
import java.net.URL
import java.net.HttpURLConnection
import java.net.PasswordAuthentication
import java.net.Authenticator

object HttpUtil {

    private const val URL_FORM_CONTENT_TYPE = "application/x-www-form-urlencoded"

    fun getRequest(requestUrl: String, bearerToken: String? = null, proxyConfig: ProxyConfiguration? = null): String {
        val proxy = if (proxyConfig != null)
            Proxy(HTTP, InetSocketAddress(proxyConfig.host, proxyConfig.port)) else NO_PROXY
        with(URL(requestUrl).openConnection(proxy) as HttpURLConnection) {
            bearerToken?.let {
                setRequestProperty("Authorization", "Bearer $bearerToken")
            }
            setRequestProperty("Content-Type", "application/json")
            return inputStream.bufferedReader().use(BufferedReader::readText)
        }
    }

    fun postRequest(requestUrl: String, requestBody: String, proxyConfig: ProxyConfiguration?): String {
        val proxy = if (proxyConfig != null)
            Proxy(HTTP, InetSocketAddress(proxyConfig.host, proxyConfig.port)) else NO_PROXY
        with(URL(requestUrl).openConnection(proxy) as HttpURLConnection) {
            requestMethod = "POST"
            setRequestProperty("Content-Type", URL_FORM_CONTENT_TYPE)
            doOutput = true
            val wr = OutputStreamWriter(outputStream);
            wr.write(requestBody);
            wr.flush();
            return inputStream.bufferedReader().use(BufferedReader::readText)
        }
    }

    private class CustomAuthenticator(val user: String, val password: String) : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication? {
            return PasswordAuthentication(user, password.toCharArray())
        }
    }

    fun setAuthenticator(user: String, password: String) {
        Authenticator.setDefault(CustomAuthenticator(user, password))
    }
}

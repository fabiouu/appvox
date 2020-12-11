package com.appvox.core.utils

import com.appvox.core.configuration.ProxyConfiguration
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.*

object HttpUtils {

    private const val URL_FORM_CONTENT_TYPE = "application/x-www-form-urlencoded"

    fun getRequest(requestUrl: String, bearerToken: String? = null, proxyConfig: ProxyConfiguration? = null) : String {
        var conn : URLConnection
        if (null != proxyConfig) {
            val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(proxyConfig.host!!, proxyConfig.port!!.toInt()))
            conn = URL(requestUrl).openConnection(proxy)
            if (null != proxyConfig.user && null != proxyConfig.password) {
                val authenticator: Authenticator = object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication? {
                        return PasswordAuthentication(proxyConfig.user, proxyConfig.password.toCharArray())
                    }
                }
                Authenticator.setDefault(authenticator)
            }
        } else {
            conn = URL(requestUrl).openConnection()
        }

        if (null != bearerToken) {
            conn.setRequestProperty("Authorization", "Bearer $bearerToken")
        }
        conn.setRequestProperty("Content-Type", "application/json")

        var response = StringBuffer()
        val reader = BufferedReader(InputStreamReader(conn.getInputStream()));
        while (true) {
            val line = reader.readLine() ?: break
            response.append(line);
        }
        reader.close()
        return response.toString()
    }

    fun postRequest(requestUrl: String, requestBody: String, configuration: ProxyConfiguration? = null) : String {
        var conn : URLConnection
        if (null != configuration) {
            val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(configuration.host!!, configuration.port!!.toInt()))
            conn = URL(requestUrl).openConnection(proxy)
            if (null != configuration.user && null != configuration.password) {
                val authenticator: Authenticator = object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication? {
                        return PasswordAuthentication(configuration.user, configuration.password.toCharArray())
                    }
                }
                Authenticator.setDefault(authenticator)
            }
        } else {
            conn = URL(requestUrl).openConnection()
        }

        conn.setRequestProperty("Content-Type", URL_FORM_CONTENT_TYPE);
        conn.doOutput = true

        val writer = OutputStreamWriter(conn.getOutputStream())

        writer.write(requestBody)
        writer.flush()

        var response = StringBuffer()
        val reader = BufferedReader(InputStreamReader(conn.getInputStream()))
        while (true) {
            val line = reader.readLine() ?: break
            response.append(line);
        }
        writer.close()
        reader.close()
        return response.toString()
    }
}
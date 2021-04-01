package dev.fabiou.appvox.core.util

import dev.fabiou.appvox.core.configuration.ProxyConfiguration
import dev.fabiou.appvox.core.exception.AppVoxErrorCode
import dev.fabiou.appvox.core.exception.AppVoxException
import java.io.*
import java.net.*

object HttpUtil {

    private const val URL_FORM_CONTENT_TYPE = "application/x-www-form-urlencoded"

    fun getRequest(requestUrl: String, bearerToken: String? = null, proxyConfig: ProxyConfiguration? = null) : String {
        val conn: URLConnection
        var reader: BufferedReader? = null
        var inputStream: InputStream? = null
        var response: String
        try {
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

            val responseBuffer = StringBuffer()
            inputStream = conn.getInputStream()
            reader = BufferedReader(InputStreamReader(inputStream));
            while (true) {
                val line = reader.readLine() ?: break
                responseBuffer.append(line);
            }
            response = responseBuffer.toString()
        } catch (e : Exception) {
            throw AppVoxException(e, AppVoxErrorCode.NETWORK)
        } finally {
            try {
                inputStream?.close()
                reader?.close()
            } catch (e: IOException) {
                throw AppVoxException(e, AppVoxErrorCode.NETWORK)
            }
        }
        return response
    }

    fun postRequest(requestUrl: String, requestBody: String, configuration: ProxyConfiguration?) : String {
        var response: String = ""
        var conn: URLConnection
        var outputStream: OutputStream? = null
        var writer: OutputStreamWriter? = null
        var reader: BufferedReader? = null
         try {
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
            outputStream = conn.getOutputStream()
            writer = OutputStreamWriter(outputStream)

            writer.write(requestBody)
            writer.flush()

            val responseBuffer = StringBuffer()
            reader = BufferedReader(InputStreamReader(conn.getInputStream()))
            while (true) {
                val line = reader.readLine() ?: break
                responseBuffer.append(line)
            }
            response = responseBuffer.toString()
        } catch (e: Exception) {

        } finally {
            outputStream?.close()
            writer?.close()
            reader?.close()
        }
        return response
    }
}
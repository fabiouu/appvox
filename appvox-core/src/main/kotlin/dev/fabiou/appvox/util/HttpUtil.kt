package dev.fabiou.appvox.util

import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.*

/**
 *
 *
 * @constructor Create empty Http util?
 */
internal object HttpUtil {

    private const val URL_FORM_CONTENT_TYPE = "application/x-www-form-urlencoded"

    fun getRequest(
        requestUrl: String,
        proxy: Proxy,
        bearerToken: String? = null,
    ): String {
        with(URL(requestUrl).openConnection(proxy) as HttpURLConnection) {
            bearerToken?.let {
                setRequestProperty("Authorization", "Bearer $bearerToken")
            }
            setRequestProperty("Content-Type", "application/json")
            return inputStream.bufferedReader().use(BufferedReader::readText)
        }
    }

    fun postRequest(
        requestUrl: String,
        requestBody: String,
        proxy: Proxy
    ): String {
        with(URL(requestUrl).openConnection(proxy) as HttpURLConnection) {
            requestMethod = "POST"
            setRequestProperty("Content-Type", URL_FORM_CONTENT_TYPE)
            doOutput = true
            val wr = OutputStreamWriter(outputStream)
            wr.write(requestBody)
            wr.flush()
            return inputStream.bufferedReader().use(BufferedReader::readText)
        }
    }

    internal fun setAuthenticator(user: String, password: CharArray) {
        Authenticator.setDefault(CustomAuthenticator(user, password))
    }

    private class CustomAuthenticator(val user: String, val password: CharArray) : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(user, password)
        }
    }
}

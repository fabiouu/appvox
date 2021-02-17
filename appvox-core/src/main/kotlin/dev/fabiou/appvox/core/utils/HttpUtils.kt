package dev.fabiou.appvox.core.utils

import dev.fabiou.appvox.core.configuration.ProxyConfiguration

interface HttpUtils {
    fun getRequest(requestUrl: String, bearerToken: String? = null, proxyConfig: ProxyConfiguration? = null) : String
    fun postRequest(requestUrl: String, requestBody: String, configuration: ProxyConfiguration? = null) : String
}
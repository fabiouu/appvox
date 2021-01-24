package com.appvox.core.utils

import com.appvox.core.configuration.ProxyConfiguration

interface HttpUtils {
    fun getRequest(requestUrl: String, bearerToken: String? = null, proxyConfig: ProxyConfiguration? = null) : String
    fun postRequest(requestUrl: String, requestBody: String, configuration: ProxyConfiguration? = null) : String
}
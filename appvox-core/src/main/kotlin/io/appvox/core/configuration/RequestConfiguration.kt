package io.appvox.core.configuration

import io.appvox.core.configuration.Constant.MIN_REQUEST_DELAY
import java.net.PasswordAuthentication
import java.net.Proxy
import java.net.Proxy.NO_PROXY

data class RequestConfiguration(
    val proxy: Proxy = NO_PROXY,
    val proxyAuthentication: PasswordAuthentication? = null,
    val delay: Int = MIN_REQUEST_DELAY
)

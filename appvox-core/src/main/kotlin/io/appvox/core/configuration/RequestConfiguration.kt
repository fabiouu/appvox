package io.appvox.configuration

import java.net.PasswordAuthentication
import java.net.Proxy
import java.net.Proxy.NO_PROXY

data class RequestConfiguration(
    val proxy: Proxy = NO_PROXY,
    val proxyAuthentication: PasswordAuthentication? = null,
    val delay: Int
)

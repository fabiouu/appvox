package io.appvox.core.configuration

import io.appvox.core.configuration.Constant.MIN_REQUEST_DELAY
import java.net.PasswordAuthentication
import java.net.Proxy
import java.net.Proxy.NO_PROXY
import kotlin.time.Duration

data class RequestConfiguration(
    val proxy: Proxy = NO_PROXY,
    val proxyAuthentication: PasswordAuthentication? = null,
    val delay: Duration = MIN_REQUEST_DELAY
)

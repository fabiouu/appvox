package dev.fabiou.appvox.core.configuration

data class RequestConfiguration(
    val proxy: ProxyConfiguration? = null,
    val requestDelay: Long
)

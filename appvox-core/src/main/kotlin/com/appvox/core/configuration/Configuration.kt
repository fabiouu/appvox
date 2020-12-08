package com.appvox.core.configuration

data class Configuration(
    val proxy: ProxyConfiguration? = null,
    val requestDelay: Long = 500
)
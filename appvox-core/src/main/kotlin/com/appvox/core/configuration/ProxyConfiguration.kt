package com.appvox.core.configuration

data class ProxyConfiguration(
        val host: String?,
        val port: Int?,
        val user: String? = null,
        val password: String? = null)
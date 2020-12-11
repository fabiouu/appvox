package com.appvox.core.search.domain.request

open class GooglePlaySearchRequest(
        val appName: String = "",
        val language: String = "en",
        val token: String? = ""
)
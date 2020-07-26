package com.appvox.core.search.domain.request

import com.appvox.core.PaginatedRequest

open class GooglePlaySearchRequest(
        val appName: String = "",
        val language: String = "en",
        val token: String? = "",
        cursor: String? = null
) : PaginatedRequest(cursor)
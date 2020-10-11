package com.appvox.core.details.domain.request

import com.appvox.core.PaginatedRequest

open class GooglePlayDetailsRequest(
        val language: String = "en",
        val sort: Int = 1,
        val size: Int = 40,
        val token: String? = "",
        cursor: String? = null
) : PaginatedRequest(cursor)
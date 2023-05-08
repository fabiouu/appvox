package io.appvox.googleplay.search.domain

import io.appvox.googleplay.review.constant.GooglePlayLanguage

data class GooglePlaySearchRequest(
    val searchTerm: String,
    val language: GooglePlayLanguage,
    val nextToken: String? = null
) {
    private constructor(builder: Builder) : this(
        searchTerm = builder.searchTerm,
        language = builder.language
    )

    class Builder {
        lateinit var searchTerm: String
        var language: GooglePlayLanguage = GooglePlayLanguage.ENGLISH_US

        fun build() = GooglePlaySearchRequest(this)
    }
}

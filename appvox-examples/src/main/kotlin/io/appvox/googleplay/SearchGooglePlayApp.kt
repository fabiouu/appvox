package io.appvox.googleplay

import io.appvox.googleplay.review.constant.GooglePlayLanguage
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    GooglePlay()
        .search {
            searchTerm = "barcode"
            language = GooglePlayLanguage.SPANISH_ES
        }
        .take(5)
        .collect { app ->
            println(app.toString())
        }
}

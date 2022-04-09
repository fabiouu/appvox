package io.appvox.itunesrss

import io.appvox.appstore.ItunesRss
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    ItunesRss()
        .reviews { appId = "333903271" }
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}

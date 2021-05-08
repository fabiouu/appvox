package com.examples.review.itunesrss

import dev.fabiou.appvox.ItunesRss
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    ItunesRss()
        .reviews("333903271")
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}

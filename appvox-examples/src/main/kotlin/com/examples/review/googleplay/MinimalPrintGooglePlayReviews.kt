package com.examples.review.googleplay

import dev.fabiou.appvox.GooglePlay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    GooglePlay().reviews("com.twitter.android")
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}

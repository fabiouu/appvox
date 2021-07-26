package com.examples.review.googleplay

import io.appvox.GooglePlay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
fun main() = runBlocking {
    GooglePlay().reviews(
        appId = "com.twitter.android")
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}

package com.company.project.googleplay

import io.appvox.googleplay.GooglePlay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    GooglePlay()
        .reviews { appId = "com.twitter.android" }
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}

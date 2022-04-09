package io.appvox.googleplay

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

package io.appvox.appstore

import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    AppStore()
        .reviews { appId = "333903271" }
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}

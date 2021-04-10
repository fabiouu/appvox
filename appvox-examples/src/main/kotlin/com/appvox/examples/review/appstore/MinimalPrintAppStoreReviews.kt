package com.appvox.examples.review.appstore

import dev.fabiou.appvox.core.AppStore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    AppStore().reviews("333903271")
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}

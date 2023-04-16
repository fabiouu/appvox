package io.appvox.googleplay

import io.appvox.googleplay.review.constant.GooglePlaySortType
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    GooglePlay()
        .reviews {
            appId = "com.gamma.scan"
            sortType = GooglePlaySortType.RELEVANT
            fetchHistory = true
        }
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}

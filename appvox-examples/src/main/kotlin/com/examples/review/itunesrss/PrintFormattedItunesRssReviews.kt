package com.examples.review.itunesrss

import dev.fabiou.appvox.ItunesRss
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion
import dev.fabiou.appvox.review.itunesrss.constant.ItunesRssSortType
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.collect

/**
 * In this example, we print the 100 most relevant App Store Reviews of the Twitter App
 * Link: https://apps.apple.com/us/app/twitter/id333903271
 *
 * Network requests are made through a proxy with a delay of 3 seconds between each request.
 * The proxy is optional and can be removed from AppReview constructor.
 * AppVox is polite by default, request delay cannot be inferior to 500 ms
 */
fun main() = runBlocking {

    val appId = "333903271"
    val userRegion = "us"
    val maxReviewCount = 100

    val config = RequestConfiguration(
        delay = 3000
    )
    val appStore = ItunesRss(config)
    appStore.reviews(
            appId = appId,
            region = AppStoreRegion.fromValue(userRegion),
            sortType = ItunesRssSortType.RECENT
        )
        .take(maxReviewCount)
        .collect { review ->
            val formattedReview =
                """
                            Id: ${review.id}
                            Rating: ${review.rating}
                            User Name: ${review.userName}
                            Title: ${review.title}
                            Comment: ${review.comment}
                            Comment Time: ${review.commentTime}
                            Review Url: ${review.url}
                        """.trimIndent()
            println(formattedReview)
        }
}

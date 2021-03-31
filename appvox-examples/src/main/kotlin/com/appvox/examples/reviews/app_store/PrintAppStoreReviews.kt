package dev.fabiou.appvox.examples.reviews.app_store

import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.configuration.ProxyConfiguration
import dev.fabiou.appvox.core.appstore.review.constant.AppStoreSortType
import dev.fabiou.appvox.core.review.googleplay.review.facade.AppReview

/*
    In this example, we print the 100 most relevant App Store Reviews of the Twitter App
    Link: https://apps.apple.com/us/app/twitter/id333903271

    Network requests are made through a proxy with a delay of 3 seconds between each request.
    The proxy is optional and can be removed from AppReview constructor.
    AppVox is polite by default, request delay cannot be inferior to 500 ms
 */
fun main(args: Array<String>) {

    val appId = "333903271"
    val userRegion = "us"
    val maxReviewCount = 100

    val config = RequestConfiguration(
            proxy = ProxyConfiguration(
                    host = "",
                    port = 0
            ),
            requestDelay = 3000L
    )
    val appReview = AppReview(config)

    appReview
        .appStore(
            appId = appId,
            region = userRegion,
            sortType = AppStoreSortType.RECENT,
            maxCount = maxReviewCount)
        .forEach { review ->
            val formattedReview =
                """
                            Id: ${review.id}
                            Rating: ${review.rating}
                            User Name: ${review.userName}
                            Title: ${review.title}
                            Comment: ${review.comment}
                            Comment Time: ${review.commentTime}
                            Reply Comment: ${review.replyComment}
                            Reply Time: ${review.replyTime}
                            Review Url: ${review.url}
                        """.trimIndent()
            println(formattedReview)
        }
}
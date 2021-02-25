package dev.fabiou.appvox.examples.reviews.google_play

import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.configuration.ProxyConfiguration
import dev.fabiou.appvox.core.review.query.AppReview
import dev.fabiou.appvox.core.review.constant.GooglePlayLanguage.ENGLISH_US
import dev.fabiou.appvox.core.review.constant.GooglePlaySortType.RELEVANT

/*
    In this example, we print the 100 most relevant Google Play Reviews of the Twitter App
    Link: https://play.google.com/store/apps/details?id=com.twitter.android&hl=en_US&gl=US

    Network requests are made through a proxy with a delay of 3 seconds between each request.
    The proxy is optional and can be removed from AppReview constructor.
    AppVox is polite by default, request delay cannot be inferior to 500 ms
 */
fun main(args: Array<String>) {

    val appId = "com.twitter.android"
    val sortType = RELEVANT
    val reviewLanguage = ENGLISH_US
    val maxReviewCount = 100

    val config = Configuration(
            proxy = ProxyConfiguration(
                    host = "",
                    port = 0
            ),
            requestDelay = 3000L
    )
    val appReview = AppReview(config)

    appReview
            .googlePlay(
                    appId = appId,
                    sortType = sortType,
                    language = reviewLanguage,
                    maxCount = maxReviewCount)
            .forEach { review ->
                val formattedReview =
                        """
                            Id: ${review.id}
                            Rating: ${review.rating}
                            User Name: ${review.userName}
                            User Avatar: ${review.userAvatar}
                            Title: ${review.title}
                            Comment: ${review.comment}
                            Comment Time: ${review.commentTime}
                            App Version: ${review.appVersion}
                            Like Count: ${review.likeCount}
                            Reply Comment: ${review.replyComment}
                            Reply Time: ${review.replyTime}
                            Review Url: ${review.url}
                        """.trimIndent()
                println(formattedReview)
            }
}
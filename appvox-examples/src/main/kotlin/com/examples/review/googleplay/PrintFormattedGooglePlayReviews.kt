package com.examples.review.googleplay

import dev.fabiou.appvox.GooglePlay
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.review.googleplay.constant.GooglePlaySortType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlin.contracts.ExperimentalContracts

/**
 *   In this example, we print the 100 most relevant Google Play Reviews of the Twitter App
 *  Link: https://play.google.com/store/apps/details?id=com.twitter.android&hl=en_US&gl=US
 *   Network requests are made through a proxy with a delay of 3 seconds between each request.
 *   The proxy is optional and can be added to GooglePlay constructor.
 *   AppVox is polite by default, request delay cannot be inferior to 500 ms
 */
@ExperimentalContracts
fun main() = runBlocking {

    val appId = "com.twitter.android"
    val sortType = GooglePlaySortType.RELEVANT
    val reviewLanguage = GooglePlayLanguage.ENGLISH_US
    val maxReviewCount = 100

    val config = RequestConfiguration(
//        proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("localhost", 8080)),
//        proxyAuthentication = PasswordAuthentication("my-proxy-username", "my-proxy-password".toCharArray()),
        delay = 3000
    )
    val googlePlay = GooglePlay(config)
    googlePlay.reviews(
        appId = appId,
        sortType = sortType,
        language = reviewLanguage
    )
        .take(maxReviewCount)
        .collect { review ->
            val formattedReview =
                """
                            Id: ${review.id}
                            Rating: ${review.rating}
                            User Name: ${review.userName}
                            User Avatar: ${review.userAvatar}
                            Title: ${review.title}
                            Comment: ${review.latestComment.text}
                            Comment Time: ${review.latestComment.time}
                            App Version: ${review.appVersion}
                            Like Count: ${review.likeCount}
                            Reply Comment: ${review.replyComment}
                            Reply Time: ${review.replyTime}
                            Review Url: ${review.url}
                        """.trimIndent()
            println(formattedReview)
        }
}

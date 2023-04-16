package io.appvox.googleplay

import io.appvox.core.configuration.RequestConfiguration
import io.appvox.googleplay.review.constant.GooglePlayLanguage
import io.appvox.googleplay.review.constant.GooglePlaySortType
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds

/**
 *   In this example, we print the 100 most relevant Google Play Reviews of the Twitter App
 *  Link: https://play.google.com/store/apps/details?id=com.twitter.android&hl=en_US&gl=US
 *   Network requests are made through a proxy with a delay of 3 seconds between each request.
 *   The proxy is optional and can be added to GooglePlay constructor.
 *   AppVox is polite by default, request delay cannot be inferior to 500 ms
 */
fun main() = runBlocking {

    val appId = "com.twitter.android"
    val sortType = GooglePlaySortType.RELEVANT
    val reviewLanguage = GooglePlayLanguage.ENGLISH_US
    val maxReviewCount = 100

    val config = RequestConfiguration(
//        proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("localhost", 8080)),
//        proxyAuthentication = PasswordAuthentication("my-proxy-username", "my-proxy-password".toCharArray()),
        delay = 3000.milliseconds
    )
    val googlePlay = GooglePlay(config)
    googlePlay.reviews {
        this.appId = appId
        this.sortType = sortType
        language = reviewLanguage
    }
        .take(maxReviewCount)
        .collect { review ->
            val formattedReview =
                """
                            Id: ${review.id}
                            Rating: ${review.rating}
                            User Name: ${review.username}
                            User Avatar: ${review.avatar}
                            Title: ${review.title}
                            Comment: ${review.text}
                            Comment Time: ${review.latestUpdateTime}
                            App Version: ${review.appVersion}
                            Like Count: ${review.likeCount}
                            Reply Comment: ${review.latestDeveloperComment?.text}
                            Reply Time: ${review.latestDeveloperComment?.latestUpdateTime}
                        """.trimIndent()
            println(formattedReview)
        }
}

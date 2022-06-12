package io.appvox.googleplay

import io.appvox.core.configuration.RequestConfiguration
import io.appvox.googleplay.review.constant.GooglePlayLanguage
import io.appvox.googleplay.review.constant.GooglePlaySortType
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

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
        delay = 3000
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
                            Rating: ${review.latestUserComment.rating}
                            User Name: ${review.latestUserComment.name}
                            User Avatar: ${review.latestUserComment.avatar}
                            Title: ${review.latestUserComment.title}
                            Comment: ${review.latestUserComment.text}
                            Comment Time: ${review.latestUserComment.lastUpdateTime}
                            App Version: ${review.latestUserComment.appVersion}
                            Like Count: ${review.latestUserComment.likeCount}
                            Reply Comment: ${review.latestDeveloperComment?.text}
                            Reply Time: ${review.latestDeveloperComment?.lastUpdateTime}
                            Review Url: ${review.url}
                        """.trimIndent()
            println(formattedReview)
        }
}

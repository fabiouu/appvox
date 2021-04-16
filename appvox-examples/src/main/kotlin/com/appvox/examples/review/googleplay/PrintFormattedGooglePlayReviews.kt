package com.appvox.examples.review.googleplay

import dev.fabiou.appvox.core.GooglePlay
import dev.fabiou.appvox.core.configuration.RequestConfiguration
import dev.fabiou.appvox.core.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.review.googleplay.constant.GooglePlaySortType
import kotlinx.coroutines.flow.collect
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

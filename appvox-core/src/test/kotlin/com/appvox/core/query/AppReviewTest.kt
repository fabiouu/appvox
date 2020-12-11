package com.appvox.core.query

import com.appvox.core.configuration.Configuration
import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.constant.GooglePlayLanguage
import com.appvox.core.review.constant.GooglePlayLanguage.*
import com.appvox.core.review.constant.GooglePlaySortType.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppReviewTest {

    @ParameterizedTest
    @CsvSource(
            "333903271, us, 50, 200, 10"
    )
    fun `Get App Store reviews using iterator`(
            appId: String,
            region: String,
            fetchCountLimit: Int,
            expectedResponseCode: Int,
            expectedSize: Int) {
        val config = Configuration(
            proxy = ProxyConfiguration(
                host = "127.0.0.1",
                port = 1087),
            requestDelay = 3000
        )
        val appReview = AppReview(config)
        appReview
                .appStore(
                        appId = "785385147",
                        region = region,
                        fetchCountlimit = fetchCountLimit)
                .forEach { review ->
                    val formattedReview =
                            """
                            Id: ${review.id}
                            Rating: ${review.rating}
                            User Name: ${review.userName}
                            Title: ${review.title}
                            Comment: ${review.comment}
                            Comment Time: ${review.commentTime}
                            Like Count: ${review.likeCount}
                            Reply Comment: ${review.replyComment}
                            Reply Time: ${review.replyCommentTime}
                            Review Url: ${review.url}
                            
                        """.trimIndent()
                    println(formattedReview)
                }
    }

    @ParameterizedTest
    @CsvSource(
            "com.twitter.android, 50, 50"
    )
    fun `Get Google Play reviews using iterator and minimal parameters`(
            appId: String,
            requestedFetchReviewCount: Int,
            expectedFetchedReviewCount: Int) {
        val config = Configuration(
            proxy = ProxyConfiguration(
                host = "127.0.0.1",
                port = 1087),
            requestDelay = 3000
        )
        var fetchedReviewCount = 0
        val appReview = AppReview(config)
        appReview
                .googlePlay(
                        appId = appId,
                        language = ENGLISH_US,
                        fetchCountlimit = requestedFetchReviewCount)
                .forEach {
                    fetchedReviewCount++
                }

        Assertions.assertEquals(expectedFetchedReviewCount, requestedFetchReviewCount)
    }

    @ParameterizedTest
    @CsvSource(
            "com.twitter.android, en-US, 1, 44, 50, 100, 200"
    )
    fun `Get most relevant Google Play reviews using iterator by batch of N reviews`(
            appId: String,
            language: String,
            sortType: Int,
            batchSize: Int,
            fetchCountLimit: Int,
            expectedResponseStatus: Int) {

        val config = Configuration(
            proxy = ProxyConfiguration(
                host = "127.0.0.1",
                port = 1087),
            requestDelay = 3000
        )
        val appReview = AppReview(config)
        appReview
                .googlePlay(
                        appId = appId,
                        language = GooglePlayLanguage.fromValue(language),
                        sortType = RELEVANT,
                        fetchCountlimit = fetchCountLimit,
                        batchSize = batchSize)
                .forEach {

                }
    }
}
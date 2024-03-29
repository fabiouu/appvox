package com.company.project.googleplay

import com.opencsv.CSVWriter
import io.appvox.core.configuration.RequestConfiguration
import io.appvox.core.exception.AppVoxException
import io.appvox.googleplay.GooglePlay
import io.appvox.googleplay.review.constant.GooglePlayLanguage
import io.appvox.googleplay.review.constant.GooglePlaySortType
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import java.io.FileWriter
import java.io.IOException
import java.util.*
import kotlin.time.Duration.Companion.milliseconds

fun main() = runBlocking {
    val appId = "com.twitter.android"
    val sortType = GooglePlaySortType.RELEVANT
    val reviewLanguage = GooglePlayLanguage.ENGLISH_US
    val maxReviewCount = 100

    val fileName =
        appId +
            "_${sortType.name.lowercase(Locale.getDefault())}" +
            "_${reviewLanguage.name.lowercase(Locale.getDefault())}" +
            "_$maxReviewCount.csv"
    val fileWriter = FileWriter(fileName)

    try {
        val csvWriter = CSVWriter(
            fileWriter,
            CSVWriter.DEFAULT_SEPARATOR,
            CSVWriter.DEFAULT_QUOTE_CHARACTER,
            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
            CSVWriter.DEFAULT_LINE_END
        )
        val columns: Array<String> =
            arrayOf(
                "id", "rating", "userName", "userProfile",
                "title", "comment", "commentTime", "appVersion",
                "likeCount", "replyComment", "replyTime", "url"
            )
        csvWriter.writeNext(columns)

        val config = RequestConfiguration(
//        proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("localhost", 8080)),
//        proxyAuthentication = PasswordAuthentication("my-proxy-username", "my-proxy-password".toCharArray()),
            delay = 5000.milliseconds
        )
        GooglePlay(config).reviews {
            this.appId = appId
            this.sortType = sortType
            language = reviewLanguage
            batchSize = 40
        }
            .take(maxReviewCount)
            .collect { review ->
                val csvReview: Array<String?> = arrayOf(
                    review.id,
                    review.rating.toString(),
                    review.username,
                    review.avatar,
                    review.title,
                    review.text,
                    review.latestUpdateTime.toString(),
                    review.appVersion,
                    review.likeCount.toString(),
                    review.text,
                    review.latestUpdateTime.toString(),
                )
                csvWriter.writeNext(csvReview)
            }
    } catch (e: AppVoxException) {
        e.printStackTrace()
    } finally {
        try {
            fileWriter.flush()
            fileWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

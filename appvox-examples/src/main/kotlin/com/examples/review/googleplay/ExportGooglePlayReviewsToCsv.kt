package com.examples.review.googleplay

import com.opencsv.CSVWriter
import dev.fabiou.appvox.GooglePlay
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.exception.AppVoxException
import dev.fabiou.appvox.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.review.googleplay.constant.GooglePlaySortType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import java.io.FileWriter
import java.io.IOException

fun main() = runBlocking {
    val appId = "com.twitter.android"
    val sortType = GooglePlaySortType.RELEVANT
    val reviewLanguage = GooglePlayLanguage.ENGLISH_US
    val maxReviewCount = 100

    val fileName =
        appId +
            "_${sortType.name.toLowerCase()}" +
            "_${reviewLanguage.name.toLowerCase()}" +
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
            delay = 3000
        )
        GooglePlay(config).reviews(
            appId = appId,
            sortType = sortType,
            language = reviewLanguage
        )
            .take(maxReviewCount)
            .collect { review ->
                val csvReview: Array<String?> = arrayOf(
                    review.id,
                    review.rating.toString(),
                    review.userName,
                    review.userAvatar,
                    review.title,
                    review.comment,
                    review.commentTime.toString(),
                    review.appVersion,
                    review.likeCount.toString(),
                    review.replyComment,
                    review.replyTime.toString(),
                    review.url
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
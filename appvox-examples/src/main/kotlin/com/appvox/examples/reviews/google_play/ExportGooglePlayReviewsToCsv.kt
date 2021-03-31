package dev.fabiou.appvox.examples.reviews.google_play

import dev.fabiou.appvox.core.configuration.ProxyConfiguration
import dev.fabiou.appvox.core.review.googleplay.review.facade.AppReview
import dev.fabiou.appvox.core.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.core.review.googleplay.constant.GooglePlaySortType
import com.opencsv.CSVWriter
import java.io.FileWriter
import java.io.IOException

fun main(args: Array<String>) {

    val appId = "com.twitter.android"
    val sortType = GooglePlaySortType.RELEVANT
    val reviewLanguage = GooglePlayLanguage.ENGLISH_US
    val maxReviewCount = 100

    val fileName =
            "$appId" +
                    "_${sortType.name.toLowerCase()}" +
                    "_${reviewLanguage.name.toLowerCase()}" +
                    "_${maxReviewCount}.csv"
    var fileWriter = FileWriter(fileName)

    try {
        var csvWriter = CSVWriter(
                fileWriter,
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END
        )
        val columns: Array<String> =
                arrayOf("id", "rating", "userName", "userProfile",
                        "title", "comment", "commentTime", "appVersion",
                        "likeCount", "replyComment", "replyTime", "url")
        csvWriter.writeNext(columns)

        val config = dev.fabiou.appvox.core.configuration.RequestConfiguration(
                proxy = ProxyConfiguration(
                        host = "",
                        port = 0
                ),
                requestDelay = 3000L
        )

        AppReview(config)
                .googlePlay(
                        appId = appId,
                        sortType = sortType,
                        language = reviewLanguage,
                        maxCount = maxReviewCount)
                .forEach { review ->
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

    } catch (e: Exception) {
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
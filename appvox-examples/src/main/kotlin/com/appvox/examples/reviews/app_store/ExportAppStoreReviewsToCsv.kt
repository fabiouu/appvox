package com.appvox.examples.reviews.app_store

import com.appvox.core.configuration.Configuration
import com.appvox.core.configuration.ProxyConfiguration
import com.appvox.core.review.query.AppReview
import com.appvox.core.review.constant.GooglePlayLanguage
import com.appvox.core.review.constant.GooglePlaySortType
import com.opencsv.CSVWriter
import java.io.FileWriter
import java.io.IOException

fun main(args: Array<String>) {

    val appId = "333903271"
    val userRegion = "us"
    val requestReviewCount = 100

    val fileName =
            "${appId}" +
                    "_${userRegion.toLowerCase()}" +
                    "_${requestReviewCount}.csv"
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
                arrayOf("id", "rating", "userName",
                        "title", "comment", "commentTime",
                        "replyComment", "replyTime", "url")
        csvWriter.writeNext(columns)

        val config = Configuration(
                proxy = ProxyConfiguration(
                        host = "",
                        port = 0
                ),
                requestDelay = 3000L
        )

        AppReview(config)
                .appStore(
                        appId = appId,
                        region = userRegion,
                        fetchCountLimit = requestReviewCount)
                .forEach { review ->
                    val csvReview: Array<String?> = arrayOf(
                            review.id,
                            review.rating.toString(),
                            review.userName,
                            review.title,
                            review.comment,
                            review.commentTime.toString(),
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
            fileWriter!!.flush()
            fileWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
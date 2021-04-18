package com.examples.review.appstore

import com.opencsv.CSVWriter
import dev.fabiou.appvox.AppStore
import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.exception.AppVoxException
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreSortType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import java.io.FileWriter
import java.io.IOException

fun main() = runBlocking {

    val appId = "333903271"
    val userRegion = "us"
    val maxReviewCount = 100

    val fileName = "${appId}_${userRegion.toLowerCase()}_$maxReviewCount.csv"
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
                "id", "rating", "userName",
                "title", "comment", "commentTime",
                "replyComment", "replyTime", "url"
            )
        csvWriter.writeNext(columns)

        val config = RequestConfiguration(
            delay = 3000
        )

        val appStore = AppStore(config)
        appStore.reviews(
                appId = appId,
                region = AppStoreRegion.fromValue(userRegion),
                sortType = AppStoreSortType.RECENT
            )
            .take(maxReviewCount)
            .collect { review ->
                val csvReview: Array<String?> = arrayOf(
                    review.id,
                    review.rating.toString(),
                    review.userName,
                    review.title,
                    review.comment,
                    review.commentTime.toString(),
                    review.replyComment,
                    review.replyTime.toString(),
                    review.likeCount.toString(),
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

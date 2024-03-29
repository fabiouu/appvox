//package io.appvox.appstore
//
//import com.opencsv.CSVWriter
//import io.appvox.appstore.review.constant.AppStoreRegion
//import io.appvox.appstore.review.constant.AppStoreSortType
//import io.appvox.core.configuration.RequestConfiguration
//import io.appvox.core.exception.AppVoxException
//import kotlinx.coroutines.flow.take
//import kotlinx.coroutines.runBlocking
//import java.io.FileWriter
//import java.io.IOException
//import java.net.InetSocketAddress
//import java.net.Proxy
//
//fun main() = runBlocking {
//
//    val appId = "333903271"
//    val maxReviewCount = 100
//
//    val fileName = "${appId}_us_$maxReviewCount.csv"
//    val fileWriter = FileWriter(fileName)
//
//    try {
//        val csvWriter = CSVWriter(
//            fileWriter,
//            CSVWriter.DEFAULT_SEPARATOR,
//            CSVWriter.DEFAULT_QUOTE_CHARACTER,
//            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
//            CSVWriter.DEFAULT_LINE_END
//        )
//        val columns: Array<String> =
//            arrayOf(
//                "id", "rating", "userName",
//                "title", "appVersion", "comment", "commentTime",
//                "replyComment", "replyTime", "url"
//            )
//        csvWriter.writeNext(columns)
//
//        val config = RequestConfiguration(
//        proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("localhost", 1080)),
////        proxy = Proxy(HTTP, InetSocketAddress("localhost", 8080)),
////        proxyAuthentication = PasswordAuthentication("my-proxy-username", "my-proxy-password".toCharArray()),
//            delay = 3000
//        )
//
//        val appStore = AppStore(config)
//        appStore.reviews {
//                this.appId = appId
//                region = AppStoreRegion.UNITED_STATES
//                sortType = AppStoreSortType.RELEVANT
//        }
//            .take(maxReviewCount)
//            .collect { review ->
//                val csvReview: Array<String?> = arrayOf(
//                    review.id,
//                    review.rating.toString(),
//                    review.username,
//                    review.title,
//                    review.appVersion,
//                    review.comment,
//                    review.commentTime.toString(),
//                    review.likeCount.toString(),
//                    review.url
//                )
//                csvWriter.writeNext(csvReview)
//            }
//    } catch (e: AppVoxException) {
//        e.printStackTrace()
//    } finally {
//        try {
//            fileWriter.flush()
//            fileWriter.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//}

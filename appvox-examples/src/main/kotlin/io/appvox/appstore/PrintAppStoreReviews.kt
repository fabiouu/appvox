package io.appvox.appstore

import io.appvox.appstore.review.constant.AppStoreRegion
import io.appvox.appstore.review.constant.AppStoreSortType
import io.appvox.core.configuration.RequestConfiguration
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val appId = "333903271"
    val maxReviewCount = 100

    val config = RequestConfiguration(
//        proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("localhost", 8080)),
//        proxyAuthentication = PasswordAuthentication("my-proxy-username", "my-proxy-password".toCharArray()),
        delay = 3000
    )
    val itunesRss = AppStore(config)
    itunesRss.reviews{
            this.appId = appId
            region = AppStoreRegion.UNITED_STATES
            sortType = AppStoreSortType.RECENT
        }
        .take(maxReviewCount)
        .collect { review ->
            println(review.toString())
        }
}

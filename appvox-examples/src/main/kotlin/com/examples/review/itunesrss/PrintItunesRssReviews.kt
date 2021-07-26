package com.examples.review.itunesrss

import io.appvox.ItunesRss
import io.appvox.configuration.RequestConfiguration
import io.appvox.review.itunesrss.constant.AppStoreRegion.UNITED_STATES
import io.appvox.review.itunesrss.constant.ItunesRssSortType.RECENT
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress
import java.net.Proxy

fun main() = runBlocking {

    val appId = "333903271"
    val maxReviewCount = 100

    val config = RequestConfiguration(
//        proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("localhost", 8080)),
//        proxyAuthentication = PasswordAuthentication("my-proxy-username", "my-proxy-password".toCharArray()),
        delay = 3000
    )
    val itunesRss = ItunesRss(config)
    itunesRss.reviews(
            appId = appId,
            region = UNITED_STATES,
            sortType = RECENT
        )
        .take(maxReviewCount)
        .collect { review ->
            println(review.toString())
        }
}

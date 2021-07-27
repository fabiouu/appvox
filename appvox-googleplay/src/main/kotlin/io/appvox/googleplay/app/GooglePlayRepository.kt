package io.appvox.googleplay.app

import io.appvox.configuration.RequestConfiguration
import io.appvox.googleplay.review.constant.GooglePlayLanguage
import io.appvox.util.HttpUtil
import io.appvox.util.memoize

internal class GooglePlayRepository(
    private val config: RequestConfiguration
) {
    companion object {
        internal var APP_HP_URL_DOMAIN = "https://play.google.com"
        internal const val APP_HP_URL_PATH = "/store/apps/details"
        internal const val REQUEST_URL_PARAMS = "?id=%s&hl=%s&gl=US&showAllReviews=true"
    }

    private val httpUtils = HttpUtil

    fun getScriptParameters(appId: String, language: GooglePlayLanguage): Map<String, String> {
        val requestUrl = APP_HP_URL_DOMAIN + APP_HP_URL_PATH + REQUEST_URL_PARAMS.format(appId, language.code)
        val responseContent = httpUtils.getRequest(requestUrl = requestUrl, proxy = config.proxy)

        val blRegex = "\"cfb2h\":\"(.+?)\"".toRegex()
        val blTokenMatches = blRegex.find(responseContent)
        val blTokenMatch = blTokenMatches?.groupValues?.get(1)
        val blValue = blTokenMatch.orEmpty()

        val sidRegex = "\"FdrFJe\":\"(.+?)\"".toRegex()
        val sidTokenMatches = sidRegex.find(responseContent)
        val sidTokenMatch = sidTokenMatches?.groupValues?.get(1)
        val sidValue = sidTokenMatch.orEmpty()

        return hashMapOf(
            "sid" to sidValue,
            "bl" to blValue
        )
    }

    val memoizedScriptParameters = { appId: String,
                                     language: GooglePlayLanguage ->
        this.getScriptParameters(appId, language)
    }.memoize()
}

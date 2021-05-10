package dev.fabiou.appvox.app.googleplay

import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.util.HttpUtil
import dev.fabiou.appvox.util.memoize

internal class GooglePlayRepository(
    private val config: RequestConfiguration
) {
    companion object {
        internal var APP_HP_URL_DOMAIN = "https://play.google.com"
        internal const val APP_HP_URL_PATH = "/store/apps/details?id=%s&hl=%s&gl=US&showAllReviews=true"
    }

    private val httpUtils = HttpUtil

    fun getScriptParameters(appId: String, language: GooglePlayLanguage): Map<String, String> {
        val requestUrl = APP_HP_URL_DOMAIN + APP_HP_URL_PATH.format(appId, language.langCode)
        val responseContent = httpUtils.getRequest(requestUrl = requestUrl, proxy = config.proxy)

        val blRegex = "\"cfb2h\":\"(.+?)\"".toRegex()
        val blTokenMatches = blRegex.find(responseContent)
        val blTokenMatch = blTokenMatches?.groupValues?.get(1)
        val blValue = blTokenMatch.orEmpty()

        val sidRegex = "\"FdrFJe\":\"(.+?)\"".toRegex()
        val sidTokenMatches = sidRegex.find(responseContent)
        val sidTokenMatch = sidTokenMatches?.groupValues?.get(1)
        val sidValue = sidTokenMatch.orEmpty()

        val atRegex = "\"SNlM0e\":\"(.+?)\"".toRegex()
        val atTokenMatches = atRegex.find(responseContent)
        val atTokenMatch = atTokenMatches?.groupValues?.get(1)
        val atValue = atTokenMatch.orEmpty()

        println("Script Parameters one time")

        return hashMapOf(
            "sid" to sidValue,
            "bl" to blValue,
            "at" to atValue
        )
    }

    val memoizedScriptParameters = { appId: String,
                                     language: GooglePlayLanguage ->
        this.getScriptParameters(appId, language)
    }.memoize()
}

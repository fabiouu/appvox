package dev.fabiou.appvox.app.googleplay

import dev.fabiou.appvox.configuration.RequestConfiguration
import dev.fabiou.appvox.review.googleplay.constant.GooglePlayLanguage
import dev.fabiou.appvox.util.HttpUtil

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

        // bl Value
        val regex = "\\\"cfb2h\\\":\\\"(.+?)\\\"".toRegex()
        val tokenMatches = regex.find(responseContent)
        val tokenMatch = tokenMatches?.groupValues?.get(1)
        val blValue = tokenMatch.orEmpty()

        // f.sid
        val regex2 = "\\\"FdrFJe\\\":\\\"(.+?)\\\"".toRegex()
        val tokenMatches2 = regex2.find(responseContent)
        val tokenMatch2 = tokenMatches2?.groupValues?.get(1)
        val sidValue = tokenMatch2.orEmpty()

        // at
        val regex3 = "\\\"SNlM0e\\\":\\\"(.+?)\\\"".toRegex()
        val tokenMatches3 = regex3.find(responseContent)
        val tokenMatch3 = tokenMatches3?.groupValues?.get(1)
        val atValue = tokenMatch3.orEmpty()

        return hashMapOf(
            "sid" to sidValue,
            "bl" to blValue,
            "at" to atValue
        )
    }


}

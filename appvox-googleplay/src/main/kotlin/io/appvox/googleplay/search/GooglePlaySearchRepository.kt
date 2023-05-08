package io.appvox.googleplay.search

import com.fasterxml.jackson.databind.ObjectMapper
import io.appvox.core.configuration.RequestConfiguration
import io.appvox.core.review.ReviewResult
import io.appvox.core.util.HttpUtil
import io.appvox.core.util.JsonUtil.getJsonNodeByIndex
import io.appvox.googleplay.search.domain.GooglePlaySearch
import io.appvox.googleplay.search.domain.GooglePlaySearchRequest

internal class GooglePlaySearchRepository(
    private val config: RequestConfiguration
) {
    companion object {
        internal var GOOGLE_PLAY_DOMAIN = "https://play.google.com"
        internal const val APP_SEARCH_URL_PATH = "/store/search"
        internal const val APP_SEARCH_URL_PARAMS = "?c=apps&q=%s&hl=%s"//TODO &gl=%s

        private const val APP_SEARCH_RESULTS_REGEX_PATTERN =
            "AF_initDataCallback\\(\\{key: 'ds:4', hash: '[\\s\\S]', data:([\\s\\S]*?), sideChannel: \\{\\}}\\);<\\/script"

        val SUB_ARRAY_INDEX = intArrayOf(0, 1, 0)
        val MANY_APP_ID_INDEX = intArrayOf(0, 0, 0)
        val MANY_TITLE_INDEX = intArrayOf(0, 3)

        val SINGLE_APP_ROOT_INDEX = intArrayOf(23, 16)
        val SINGLE_APP_ID_INDEX = intArrayOf(11, 0, 0)
        val SINGLE_TITLE_INDEX = intArrayOf(2, 0, 0)
    }

    private val httpUtil = HttpUtil

    fun searchAppByTerm(request: GooglePlaySearchRequest): ReviewResult<GooglePlaySearch> {
        val requestUrl = GOOGLE_PLAY_DOMAIN + APP_SEARCH_URL_PATH +
            APP_SEARCH_URL_PARAMS.format(request.searchTerm, request.language.code)
        val responseContent = httpUtil.getRequest(requestUrl = requestUrl, proxy = config.proxy)
        val searchResultApps = mutableListOf<GooglePlaySearch>()
        val data = extractData(responseContent)
        val rootNode = ObjectMapper().readTree(data)
        val subNode = getJsonNodeByIndex(rootNode, SUB_ARRAY_INDEX)
        var apps = subNode[22]
        if (apps.isNull) {
            val singleApp = getJsonNodeByIndex(subNode, SINGLE_APP_ROOT_INDEX)
            val searchResultApp = GooglePlaySearch(
                appId = getJsonNodeByIndex(singleApp, SINGLE_APP_ID_INDEX).asText(),
                appTitle = getJsonNodeByIndex(singleApp, SINGLE_TITLE_INDEX).asText()
            )
            searchResultApps.add(searchResultApp)
        } else {
            for (app in apps[0]) {
                val searchResultApp = GooglePlaySearch(
                    appId = getJsonNodeByIndex(app, MANY_APP_ID_INDEX).asText(),
                    appTitle = getJsonNodeByIndex(app, MANY_TITLE_INDEX).asText()
                )
                searchResultApps.add(searchResultApp)
            }
        }
        return ReviewResult(
            results = searchResultApps,
            nextToken = null
        )
    }

    private fun extractData(content: String): String? {
        val regex = APP_SEARCH_RESULTS_REGEX_PATTERN.toRegex()
        return regex.find(content)?.let { tokenMatch ->
            return tokenMatch.groupValues[1]
        }
    }
}

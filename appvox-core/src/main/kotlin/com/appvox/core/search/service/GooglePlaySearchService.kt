//package com.appvox.core.search.service
//
//import com.appvox.core.search.domain.request.GooglePlaySearchRequest
//import com.appvox.core.search.domain.result.GooglePlaySearchResult
//import com.appvox.core.search.domain.result.GooglePlaySearchResults
//import com.appvox.core.utils.HttpUtils
//import com.appvox.core.utils.JsonUtils
//import com.appvox.core.utils.JsonUtils.getJsonNodeByIndex
//import com.fasterxml.jackson.databind.JsonNode
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.github.kittinunf.fuel.core.FuelManager
//import com.github.kittinunf.fuel.core.Headers.Companion.CONTENT_TYPE
//import com.github.kittinunf.fuel.httpPost
//import java.net.InetSocketAddress
//import java.net.Proxy
//
//object GooglePlaySearchService {
//    private const val requestUrl : String = "https://play.google.com/_/PlayStoreUi/data/batchexecute?rpcids=lGYRle&f.sid=845909587187715220&bl=boq_playuiserver_20200722.03_p0&hl=%s&soc-app=121&soc-platform=1&soc-device=1&authuser=&_reqid=841251&rt=c"
//    private const val initialRequestBody = "f.req=[[[\"lGYRle\",\"[[[],[[10,[10,50]],true,null,[96,27,4,8,57,30,110,11,16,49,1,3,9,12,104,55,56,51,10,34,31,77,145],[null,null,null,[[[[7,31],[[1,52,43,112,92,58,69,31,19,96,103]]]]]]],[\\\"%s\\\"],4,[null,1],null,null,[]]]\",null,\"2\"]]]"
////    private const val requestBodyWithToken : String = "f.req=[[[\"UsvDTd\",\"[null,null,[2,null,[%d,null,\\\"%s\\\"],null,[]],[\\\"%s\\\",7]]\",null,\"generic\"]]]"
//
//    private val APP_NAME_INDEX = arrayOf(2)
//    private val APP_PIC_INDEX = arrayOf(1, 1, 0, 3, 2)
//    private val APP_DESCRIPTION_INDEX = arrayOf(4,1,1,1,1)
//    private val APP_DEVELOPER_LINK_INDEX = arrayOf(4,0,0,1)
//    private val APP_DEVELOPER_NAME_INDEX = arrayOf(4,0,0,0)
//    private val APP_LINK_INDEX = arrayOf(9,4,2)
//    private val APP_ID_INDEX = arrayOf(12, 0)
//    private val APP_RATING_INDEX = arrayOf(6, 0, 2, 1, 0)
//    private val APP_RATING_DETAILS_INDEX = arrayOf(6, 0, 2, 1, 1)
//
//    init {
//        HttpUtils.setProxy("localhost", 1080)
//    }
//
//    fun searchAppByName(request: GooglePlaySearchRequest): GooglePlaySearchResults {
//
//        var requestBody : String
//        if (request.token != null && request.token.isNotEmpty()) {
////            requestBody = requestBodyWithToken.format()
//            requestBody = ""
//        } else {
//            requestBody = initialRequestBody.format(request.appName)
//        }
//
//        val addr = InetSocketAddress("localhost", 1080)
//        FuelManager.instance.proxy = Proxy(Proxy.Type.HTTP, addr)
//
//        val requestUrl = requestUrl.format(request.language)
//        val (request, response, result) = requestUrl
//                .httpPost()
//                .body(requestBody)
//                .header(CONTENT_TYPE,  "application/x-www-form-urlencoded")
//                .responseString()
//
//        val gplaySearchResults = extractSearchFromResponse(result.get())
//
//        var searchResults = ArrayList<GooglePlaySearchResult>()
//        for (gplaySearchResult in gplaySearchResults) {
//            val searchResult = GooglePlaySearchResult(
//                appId = getJsonNodeByIndex(gplaySearchResult, APP_ID_INDEX).asText(),
//                appName = getJsonNodeByIndex(gplaySearchResult, APP_NAME_INDEX).asText(),
//                appPic = getJsonNodeByIndex(gplaySearchResult, APP_PIC_INDEX).asText(),
//                appLink = getJsonNodeByIndex(gplaySearchResult, APP_LINK_INDEX).asText(),
//                appRating = getJsonNodeByIndex(gplaySearchResult, APP_RATING_INDEX).asText(),
//                appRatingDetails = getJsonNodeByIndex(gplaySearchResult, APP_RATING_DETAILS_INDEX).asText(),
//                appDeveloperName = getJsonNodeByIndex(gplaySearchResult, APP_DEVELOPER_NAME_INDEX).asText(),
//                appDeveloperLink = getJsonNodeByIndex(gplaySearchResult, APP_DEVELOPER_LINK_INDEX).asText(),
//                appDescription = getJsonNodeByIndex(gplaySearchResult, APP_DESCRIPTION_INDEX).asText()
//            )
//            searchResults.add(searchResult)
//        }
//
//        val token = ""//if (!gplayReviews.isEmpty && !gplayReviews[1].isEmpty) gplayReviews[1][1] else null
//        return GooglePlaySearchResults(token = token, apps = searchResults)
//    }
//
//    private fun extractSearchFromResponse(gplayResponse: String): JsonNode {
//        val cleanGplayResponse = gplayResponse.substring(12)
//        val gplayRootArray = ObjectMapper().readTree(cleanGplayResponse)
//        val gplaySubArray : JsonNode = gplayRootArray[0][2]
//        val gplaySubArrayAsJsonString = gplaySubArray.textValue()
//        val gplaySearchResponse = ObjectMapper().readTree(gplaySubArrayAsJsonString)
//        val gplaySearchResult = getJsonNodeByIndex(gplaySearchResponse, arrayOf(0,1,0,0,0))
//        return gplaySearchResult
//    }
//}
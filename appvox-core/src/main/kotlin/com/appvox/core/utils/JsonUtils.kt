package com.appvox.core.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.TextNode
import com.github.kittinunf.fuel.core.FuelManager
import java.net.InetSocketAddress
import java.net.Proxy

object JsonUtils {

    fun getJsonNodeByIndex(jsonResponse: JsonNode, nestedIndexes: Array<Int>, currentIndex: Int = 0): JsonNode {
        val responseIndex = nestedIndexes[currentIndex]

        val jsonNode: JsonNode? = jsonResponse[responseIndex]
        if (jsonNode == null || jsonNode.isNull)
            return TextNode.valueOf("")

        val nextIndex = currentIndex + 1
        if (nextIndex == nestedIndexes.size)
            return jsonNode

        return getJsonNodeByIndex(jsonNode, nestedIndexes, nextIndex)
    }
}
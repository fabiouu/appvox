package dev.fabiou.appvox.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.TextNode

internal object JsonUtil {
    fun getJsonNodeByIndex(jsonResponse: JsonNode, nestedIndexes: IntArray, currentIndex: Int = 0): JsonNode {
        val responseIndex = nestedIndexes[currentIndex]

        val jsonNode: JsonNode? = jsonResponse[responseIndex]
        if (jsonNode == null || jsonNode.isNull) {
            return TextNode.valueOf("")
        }

        val nextIndex = currentIndex + 1
        if (nextIndex == nestedIndexes.size) {
            return jsonNode
        }

        return getJsonNodeByIndex(jsonNode, nestedIndexes, nextIndex)
    }
}

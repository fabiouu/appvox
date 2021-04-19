package dev.fabiou.appvox.util

import com.fasterxml.jackson.databind.JsonNode

internal object JsonUtil {
    fun getJsonNodeByIndex(jsonResponse: JsonNode, nestedIndexes: IntArray, currentIndex: Int = 0): JsonNode {
        val responseIndex = nestedIndexes[currentIndex]

        val jsonNode: JsonNode = jsonResponse[responseIndex]
        val nextIndex = currentIndex + 1
        if (jsonNode.isNull || nextIndex == nestedIndexes.size) {
            return jsonNode
        }

        return getJsonNodeByIndex(jsonNode, nestedIndexes, nextIndex)
    }
}

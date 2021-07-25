package dev.fabiou.appvox.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode

object JsonUtil {
    fun getJsonNodeByIndex(jsonResponse: JsonNode, nestedIndexes: IntArray, currentIndex: Int = 0): JsonNode {
        val responseIndex = nestedIndexes[currentIndex]
        val jsonNode = jsonResponse[responseIndex] ?: return NullNode.getInstance()
        val nextIndex = currentIndex + 1
        if (jsonNode.isNull || nextIndex == nestedIndexes.size) {
            return jsonNode
        }
        return getJsonNodeByIndex(jsonNode, nestedIndexes, nextIndex)
    }
}

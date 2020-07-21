package com.appvox.api.helper

import org.springframework.util.Base64Utils
import java.nio.charset.Charset

class CursorHelper {
    companion object {
        fun decodeCursorToParameters(encodedCursor : String) : Map<String, String> {
            var parameters = hashMapOf<String, String>()
            val decodedCursor = Base64Utils.decodeFromString(encodedCursor)
            val decodedCursorAsString = decodedCursor.toString(Charset.defaultCharset())
            val kvParameters = decodedCursorAsString.split("|")
            kvParameters.forEach {
                val key = it.substringBefore(':')
                val value = it.substringAfter(':')
                parameters[key] = value
            }
            return parameters
        }

        fun encodeParametersToCursor(parameters: Map<String, String>) : String {
            var kvParameters = ArrayList<String>()
            parameters.forEach {
                val kvParameter = listOf(it.key, it.value).joinToString(":")
                kvParameters.add(kvParameter)
            }
            val unencodedCursor = kvParameters.joinToString("|")
            val encodedCursor = Base64Utils.encodeToUrlSafeString(unencodedCursor.toByteArray(Charset.defaultCharset()))
            return encodedCursor
        }
    }
}
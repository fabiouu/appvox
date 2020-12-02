package com.appvox.api.helper

import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.Map
import kotlin.collections.forEach
import kotlin.collections.hashMapOf
import kotlin.collections.joinToString
import kotlin.collections.listOf
import kotlin.collections.set


class CursorHelper {
    companion object {
        fun decodeCursorToParameters(encodedCursor: String) : Map<String, String> {
            var parameters = hashMapOf<String, String>()
            val decodedCursor: ByteArray = Base64.getDecoder().decode(encodedCursor)
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
            val encodedCursor: String = Base64.getEncoder().encodeToString(
                unencodedCursor.toByteArray(Charset.defaultCharset()))
            return encodedCursor
        }
    }
}
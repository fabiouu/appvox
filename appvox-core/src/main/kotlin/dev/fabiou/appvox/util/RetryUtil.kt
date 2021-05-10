package dev.fabiou.appvox.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExperimentalContracts
inline fun <T : Any> retryWithPolicy(value: T?, lazyMessage: () -> Any): T {
    contract {
        returns() implies (value != null)
    }

    if (value == null) {
        val message = lazyMessage()
        throw IllegalArgumentException(message.toString())
    } else {
        return value
    }
}

//fun <T> Flow<T>.retryWithPolicy(
//    retryPolicy: RetryPolicy
//): Flow<T> {
//    var currentDelay = retryPolicy.delayMillis
//    val delayFactor = retryPolicy.delayFactor
//    return retryWhen { cause, attempt ->
//        if (cause is IOException && attempt < retryPolicy.numRetries) {
//            delay(currentDelay)
//            currentDelay *= delayFactor
//            return@retryWhen true
//        } else {
//            return@retryWhen false
//        }
//    }
//}

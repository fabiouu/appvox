package dev.fabiou.appvox.util

import dev.fabiou.appvox.configuration.Constant.DELAY_FACTOR
import dev.fabiou.appvox.exception.AppVoxNetworkException
import kotlinx.coroutines.delay

internal suspend inline fun <R> retryRequest(
    maxAttempts: Int,
    minRetryDelay: Long,
    delayFactor: Double = DELAY_FACTOR,
    requestBlock: () -> R
): R {
    var currentDelay = minRetryDelay
    repeat(maxAttempts - 1) {
        try {
            return requestBlock()
        } catch (e: AppVoxNetworkException) {

        }
        delay(timeMillis = currentDelay)
        currentDelay = (currentDelay * delayFactor).toLong()
    }
    return requestBlock()
}

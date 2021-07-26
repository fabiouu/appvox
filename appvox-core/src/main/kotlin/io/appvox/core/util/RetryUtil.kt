package io.appvox.util

import io.appvox.configuration.Constant.DELAY_FACTOR
import io.appvox.exception.AppVoxNetworkException
import kotlinx.coroutines.delay

suspend inline fun <R> retryRequest(
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
            // We should log the retry attempt here and do not leave an empty catch block.
            // The project does not implement a logging strategy yet. Implemented in the next release (see roadmap)
        }
        delay(timeMillis = currentDelay)
        currentDelay = (currentDelay * delayFactor).toLong()
    }
    return requestBlock()
}

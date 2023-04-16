package io.appvox.core.util

import io.appvox.core.configuration.Constant.DELAY_FACTOR
import io.appvox.core.exception.AppVoxException
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

suspend inline fun <R> retryRequest(
    maxAttempts: Int,
    minRetryDelay: Duration,
    delayFactor: Double = DELAY_FACTOR,
    requestBlock: () -> R
): R {
    var currentDelay = minRetryDelay
    repeat(maxAttempts - 1) {
        try {
            return requestBlock()
        } catch (e: AppVoxException) {
            // We should log the retry attempt here and do not leave an empty catch block.
            // The project does not implement a logging strategy yet. Implemented in the next release (see roadmap)
        }
        delay(timeMillis = currentDelay.inWholeMilliseconds)
        currentDelay = (currentDelay.inWholeMilliseconds * delayFactor).milliseconds
    }
    return requestBlock()
}

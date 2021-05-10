package dev.fabiou.appvox.util

import dev.fabiou.appvox.exception.AppVoxError.NETWORK_RETRY_FAILURE
import dev.fabiou.appvox.exception.AppVoxException
import dev.fabiou.appvox.exception.AppVoxNetworkException

inline fun retryRequest(maxAttempts: Int, requestMethod: () -> Any) {
    for (attemptNo in 1..maxAttempts) {
        try {
            requestMethod()
            break
        } catch (e: AppVoxNetworkException) {
            if (attemptNo == maxAttempts) {
                throw AppVoxException(NETWORK_RETRY_FAILURE, e)
            }
        }
    }
}

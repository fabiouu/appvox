package io.appvox.core.configuration

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object Constant {

    /**
     * Minimum request delay between two network requests
     * Minimum request delay is set in an effort to provide a polite scraper that do not affect the scraped service
     * An @exception will be thrown
     */
    val MIN_REQUEST_DELAY: Duration = 500.milliseconds

    const val DEFAULT_BATCH_SIZE = 40

    val MIN_RETRY_DELAY = 3.seconds

    const val MAX_RETRY_ATTEMPTS = 5

    const val DELAY_FACTOR = 5.0
}

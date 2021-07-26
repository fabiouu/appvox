package io.appvox.configuration

object Constant {

    /**
     * Minimum request delay in milliseconds between two network requests
     * Minimum request delay is set in an effort to provide a polite scraper that do not affect the scraped service
     * An @exception will be thrown
     */
    const val MIN_REQUEST_DELAY: Int = 500

    const val DEFAULT_BATCH_SIZE = 40

    const val MIN_RETRY_DELAY = 3000L

    const val MAX_RETRY_ATTEMPTS = 5

    const val DELAY_FACTOR = 5.0
}

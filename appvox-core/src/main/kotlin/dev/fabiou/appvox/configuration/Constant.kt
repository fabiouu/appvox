package dev.fabiou.appvox.configuration

internal object Constant {

    /**
     * Minimum request delay in milliseconds between two network requests
     * Minimum request delay is set in an effort to provide a polite scraper that do not affect the scraped service
     * An @exception will be thrown
     */
    const val MIN_REQUEST_DELAY: Int = 500
}

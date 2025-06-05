package io.appvox.core.exception

enum class AppVoxError(val errorCode: Int, val errorMessage: String) {
    DESERIALIZATION(
        errorCode = 0,
        errorMessage = "Failed to deserialize network payload"
    ),
    REQ_DELAY_TOO_SHORT(
        errorCode = 1,
        errorMessage = "Specified interval between network requests is inferior to 500 ms"
    ),
    NETWORK(
        errorCode = 2,
        errorMessage = "A network issue occurred"
    ),
    INVALID_ARGUMENT(
        errorCode = 3,
        errorMessage = "Invalid scraper argument"
    ),
    RATE_LIMIT(
        errorCode = 4,
        errorMessage = "Rate Limit"
    );
}

package dev.fabiou.appvox.exception

enum class AppVoxError(val errorCode: Int, val errorMessage: String) {
    DESERIALIZATION(
        errorCode = 0,
        errorMessage = "Failed to deserialize network payload"
    ),
    REQ_DELAY_TOO_SHORT(
        errorCode = 3,
        errorMessage = "Specified interval between network requests is inferior to 500 ms"
    ),
    INVALID_ARGUMENT(
        errorCode = 4,
        errorMessage = "Invalid scraper argument");
}

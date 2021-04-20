package dev.fabiou.appvox.exception

enum class AppVoxError(val errorCode: Int, val errorMessage: String) {
    SERIALIZATION(0, "Could not deserialize network payload"),
    NETWORK(1, ""),
    REQ_DELAY_TOO_SHORT(2, "Interval or delay between network requests is inferior to 500 ms"),
    INVALID_ARGUMENT(3, "");
}

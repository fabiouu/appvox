package dev.fabiou.appvox.exception

enum class AppVoxError(val errorCode: Int, val errorMessage: String) {
    DESERIALIZATION(0, "Failed to deserialize network payload"),
    TRANSIENT_NETWORK_FAILURE(1, ""),
    REQ_DELAY_TOO_SHORT(2, "Specified interval between network requests is inferior to 500 ms"),
    INVALID_ARGUMENT(3, "");
}

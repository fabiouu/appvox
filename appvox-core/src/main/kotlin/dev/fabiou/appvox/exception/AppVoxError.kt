package dev.fabiou.appvox.exception

enum class AppVoxError(val errorCode: Int, val errorMessage: String) {
    SERIALIZATION(0, ""),
    NETWORK(1, ""),
    REQ_DELAY_TOO_SHORT(2, ""),
    INVALID_ARGUMENT(3, "");
}

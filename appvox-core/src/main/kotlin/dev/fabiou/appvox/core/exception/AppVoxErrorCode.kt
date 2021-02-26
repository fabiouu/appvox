package dev.fabiou.appvox.core.exception

enum class AppVoxErrorCode(val errorMessage: String) {
    SERIALIZATION(""),
    NETWORK(""),
    REQ_DELAY_TOO_SHORT(""),
    INVALID_ARGUMENT("");
}
package dev.fabiou.appvox.core.exception

class AppVoxException : Exception {
    private val code: AppVoxError

    constructor(code: AppVoxError) : super() {
        this.code = code
    }

    constructor(message: String?, cause: Throwable?, code: AppVoxError) : super(message, cause) {
        this.code = code
    }

    constructor(message: String?, code: AppVoxError) : super(message) {
        this.code = code
    }

    constructor(cause: Throwable?, code: AppVoxError) : super(cause) {
        this.code = code
    }

    fun getCode(): AppVoxError {
        return code
    }
}

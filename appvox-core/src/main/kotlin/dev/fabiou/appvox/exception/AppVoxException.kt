package dev.fabiou.appvox.exception

class AppVoxException : Exception {
    private val code: AppVoxError

    constructor(code: AppVoxError) : super() {
        this.code = code
    }

    constructor(code: AppVoxError, message: String?, cause: Throwable?) : super(message, cause) {
        this.code = code
    }

    constructor(code: AppVoxError, message: String?) : super(message) {
        this.code = code
    }

    constructor(code: AppVoxError, cause: Throwable?) : super(cause) {
        this.code = code
    }

    fun getCode(): AppVoxError {
        return code
    }
}

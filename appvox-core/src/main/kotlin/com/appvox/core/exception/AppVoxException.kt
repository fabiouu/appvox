package com.appvox.core.exception

/**
 *
 * @author fabiouu
 */
class AppVoxException : Exception {
    private val code: AppVoxErrorCode

    constructor(code: AppVoxErrorCode) : super() {
        this.code = code
    }

    constructor(message: String?, cause: Throwable?, code: AppVoxErrorCode) : super(message, cause) {
        this.code = code
    }

    constructor(message: String?, code: AppVoxErrorCode) : super(message) {
        this.code = code
    }

    constructor(cause: Throwable?, code: AppVoxErrorCode) : super(cause) {
        this.code = code
    }

    fun getCode(): AppVoxErrorCode {
        return code
    }
}
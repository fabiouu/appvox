package dev.fabiou.appvox.configuration

data class RequestConfiguration(
    val proxy: Proxy? = null,
    val delay: Int
) {
    data class Proxy(
        val host: String,
        val port: Int,
        val user: String? = null,
        val password: String? = null
    )
}

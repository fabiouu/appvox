package com.appvox.core.configuration

class ProxyConfiguration(
        val host: String?,
        val port: Int?,
        val user: String?,
        val password: String?) {

    private constructor(builder: Builder)
            : this(builder.host, builder.port, builder.user, builder.password)

    class Builder {
        var host: String? = null
            private set

        var port: Int = 0
            private set

        var user: String? = null
            private set

        var password: String? = null
            private set

        fun host(host: String) = apply { this.host = host }

        fun port(port: Int) = apply { this.port = port }

        fun user(user: String) = apply { this.user = user }

        fun password(password: String) = apply { this.password = password }

        fun build() = ProxyConfiguration(this)
    }
}
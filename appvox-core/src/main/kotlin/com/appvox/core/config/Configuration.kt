package com.appvox.core.config

//data class Configuration (
//        val proxyHost: String?,
//        val proxyPort: Int?,
//        val proxyUser: String?,
//        val proxyPassword: String?)
//

class Configuration(
        val proxyHost: String?,
        val proxyPort: Int?,
        val proxyUser: String?,
        val proxyPassword: String?) {

    private constructor(builder: Builder)
            : this(builder.proxyHost, builder.proxyPort, builder.proxyUser, builder.proxyPassword)

    class Builder {
        var proxyHost: String? = null
            private set

        var proxyPort: Int = 0
            private set

        var proxyUser: String? = null
            private set

        var proxyPassword: String? = null
            private set

        fun proxyHost(proxyHost: String) = apply { this.proxyHost = proxyHost }

        fun proxyPort(proxyPort: Int) = apply { this.proxyPort = proxyPort }

        fun proxyUser(proxyUser: String) = apply { this.proxyUser = proxyUser }

        fun proxyPassword(proxyPassword: String) = apply { this.proxyPassword = proxyPassword }

        fun build() = Configuration(this)
    }
}
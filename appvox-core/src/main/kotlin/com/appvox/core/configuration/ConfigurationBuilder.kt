package com.appvox.core.configuration

class ConfigurationBuilder private constructor(
    val proxyHost: String?,
    val proxyPort: Int?,
    val proxyUser: String?,
    val proxyPassword: String?) {

    data class Builder(
        var proxyHost: String? = null,
        var proxyPort: Int? = null,
        var proxyUser: String? = null,
        var proxyPassword: String? = null) {

        fun proxyHost(proxyHost: String) = apply { this.proxyHost = proxyHost }
        fun proxyPort(proxyPort: Int) = apply { this.proxyPort = proxyPort }
        fun proxyUser(proxyUser: String) = apply { this.proxyUser = proxyUser }
        fun proxyPassword(proxyPassword: String) = apply { this.proxyPassword = proxyPassword }
        fun build() = ConfigurationBuilder(proxyHost, proxyPort, proxyUser, proxyPassword)
    }
}
package com.appvox.core.configuration

class Configuration(
        val proxy: ProxyConfiguration?) {

    private constructor(builder: Builder)
            : this(builder.proxy)

    class Builder {
        var proxy: ProxyConfiguration? = null
            private set

        fun proxy(proxy: ProxyConfiguration) = apply { this.proxy = proxy }

        fun build() = Configuration(this)
    }
}
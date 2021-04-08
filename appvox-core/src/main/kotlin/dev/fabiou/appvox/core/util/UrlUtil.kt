package dev.fabiou.appvox.core.util

object UrlUtil {
    fun getUrlDomainByEnv(defaultDomain: String) : String {
        val envVar: String? = System.getenv("TRAVIS")
        return if (envVar == null || envVar.isEmpty()) "http://localhost:8080" else defaultDomain
    }
}

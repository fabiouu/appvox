object UrlUtil {
    fun getUrlDomainByEnv(defaultDomain: String) : String {
        val isCiEnv: String? = System.getenv("TRAVIS")
        return if (isCiEnv == null || isCiEnv.isEmpty()) "http://localhost:8080" else defaultDomain
    }
}

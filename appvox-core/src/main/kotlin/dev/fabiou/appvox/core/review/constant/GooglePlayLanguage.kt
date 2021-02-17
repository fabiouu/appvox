package dev.fabiou.appvox.core.review.constant

/*
    https://support.google.com/googleplay/android-developer/table/4419860
 */
enum class GooglePlayLanguage(val langCode: String) {
    AFRIKAANS("af"),
    AMHARIC("am"),
    BULGARIAN("bg"),
    CATALAN("ca"),
    CHINESE_HK("zh-HK"),
    CHINESE_CN("zh-CN"),
    CHINESE_TW("zh-TW"),
    CROATIAN("hr"),
    CZECH("cs"),
    DANISH("da"),
    DUTCH("nl"),
    ENGLISH_UK("en-GB"),
    ENGLISH_US("en-US"),
    ESTONIAN("et"),
    FILIPINO("fil"),
    FINNISH("fi"),
    FRENCH_CA("fr-CA"),
    FRENCH_FR("fr-FR"),
    GERMAN("de"),
    GREEK("el"),
    HEBREW("he"),
    HINDI("hi"),
    HUNGARIAN("hu"),
    ICELANDIC("is"),
    INDONESIAN("id"),
    ITALIAN("it"),
    JAPANESE("ja"),
    KOREAN("ko"),
    LATVIAN("lv"),
    LITHUANIAN("lt"),
    MALAY("ms"),
    NORWEGIAN("no"),
    POLISH("pl"),
    PORTUGUESE_BR("pt-BR"),
    PORTUGUESE_PT("pt-PT"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    SERBIAN("sr"),
    SLOVAK("sk"),
    SLOVENIAN("sl"),
    SPANISH_LATIN("es-419"),
    SPANISH_ES("es-ES"),
    SWAHILI("sw"),
    SWEDISH("sv"),
    THAI("th"),
    TURKISH("tr"),
    UKRAINIAN("uk"),
    VIETNAMESE("vi"),
    ZULU("zu");

    companion object {
        fun fromValue(langCode: String) : GooglePlayLanguage {
            for (googlePlayLanguage in values()) {
                if (langCode.equals(googlePlayLanguage)) {
                    return googlePlayLanguage
                }
            }
            return ENGLISH_US
        }
    }
}
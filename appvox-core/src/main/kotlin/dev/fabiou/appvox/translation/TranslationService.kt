package dev.fabiou.appvox.translation

interface TranslationService {
    fun translate(sourceText: String): String
    fun detectLanguage(source: String): String
}

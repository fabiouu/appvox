package dev.fabiou.appvox.core.translation

interface TranslationService {
    fun translate(sourceText: String): String
    fun detectLanguage(source: String): String
}
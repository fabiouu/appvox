package dev.fabiou.appvox.core.translation

class GoogleTranslationImpl : TranslationService {

    override fun translate(sourceText: String): String {
        return "test";
//        TODO("Not yet implemented")
    }

    override fun detectLanguage(source: String): String {
        TODO("Not yet implemented")
    }
}
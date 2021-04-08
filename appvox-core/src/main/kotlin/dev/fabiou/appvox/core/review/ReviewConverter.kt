package dev.fabiou.appvox.core.review

internal interface ReviewConverter<Source, Target> {
    fun toResponse(results: List<Source>): List<Target>
}

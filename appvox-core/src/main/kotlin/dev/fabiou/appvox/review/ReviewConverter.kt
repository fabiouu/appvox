package dev.fabiou.appvox.review

internal interface ReviewConverter<Source, Target> {
    fun toResponse(results: List<Source>): List<Target>
}

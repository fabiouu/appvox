package dev.fabiou.appvox.core.review

 interface ReviewConverter<Source, Target> {
    fun toResponse(reviewResults: List<Source>): List<Target>
}
package dev.fabiou.appvox.review.classification

enum class UserPersona(val persona: Int) {
    /**
     * A user with a long review history (> 6 months). Only available when review fetchHistory is activated
     */
    LOYAL(persona = 0),

    /**
     * A user who took time to write an extensive review of your application (> N characters)
     */
    TALKATIVE(persona = 1),

    /**
     * A user who wrote a very short review (< 30 characters). Usually not helpful to the App developer
     */
    QUIET(persona = 2),

    /**
     * A detractor of your application (1-2 stars review)
     */
    DETRACTOR(persona = 3),

    /**
     * A promoter of your application (4-5 stars review)
     */
    PROMOTER(persona = 4),

    /**
     * A user who lowered his rating over time. Only available when review fetchHistory is activated
     */
    DISSATISFIED(persona = 5),

    /**
     * A user accumulating more than 100 likes on his latest review
     */
    POPULAR(persona = 6)
}

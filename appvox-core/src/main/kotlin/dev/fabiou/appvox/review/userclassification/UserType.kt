package dev.fabiou.appvox.review.userclassification

enum class UserType(val type: Int) {
    /**
     * A user with a long review history (> 6 months). Only available when review fetchHistory is activated
     */
    LOYAL(type = 0),

    /**
     * A user who took time to write an extensive review of your application (> N characters)
     */
    TALKATIVE(type = 1),

    /**
     * A user who wrote a very short review (< 30 characters). Usually not helpful to the App developer
     */
    QUIET(type = 2),

    /**
     * A detractor of your application (1-2 stars review)
     */
    DETRACTOR(type = 3),

    /**
     * A promoter of your application (4-5 stars review)
     */
    PROMOTER(type = 4),

    /**
     * A user who lowered his rating over time. Only available when review fetchHistory is activated
     */
    DISSATISFIED(type = 5)
}

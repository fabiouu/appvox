package dev.fabiou.appvox.review.classification

enum class ReviewType(val type: Int) {
    /**
     * Long review of your application (> N characters)
     */
    LONG(type = 0),

    /**
     * Short review of your application (< 30 characters). Usually not helpful to the App developer
     */
    SHORT(type = 1),

    /**
     * A detractor of your application (1 and 2 stars reviews are considered negative)
     */
    NEGATIVE(type = 2),

    /**
     * A promoter of your application (4-5 stars review)
     */
    POSITIVE(type = 3)
}

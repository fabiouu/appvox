package dev.fabiou.appvox.review.googleplay.classification

enum class GooglePlayUserType(val type: Int) {
    /**
     * A user with a long review history (> 6 months).
     * Only available when fetchHistory flag is activated.
     */
    LOYAL(type = 0),

    /**
     * A detractor of your application (1->3 stars review)
     */
    DETRACTOR(type = 1),

    /**
     * A promoter of your application (4-5 stars review)
     */
    PROMOTER(type = 2),

    /**
     * A user who lowered his rating over time.
     * Only available when fetchHistory flag is activated.
     */
    DISSATISFIED(type = 3),

    /**
     * A user who raised his rating over time.
     * Only available when fetchHistory flag is activated.
     */
    SATISFIED(type = 4)
}

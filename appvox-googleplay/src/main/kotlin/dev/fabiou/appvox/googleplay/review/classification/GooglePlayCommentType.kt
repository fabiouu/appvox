package dev.fabiou.appvox.googleplay.review.classification

enum class GooglePlayCommentType(val type: Int) {
    /**
     * Long review of your application (> N characters - configurable)
     */
    EXTENSIVE(type = 0),

    /**
     * Short review of your application (< 30 characters). Usually not helpful to the App developer
     */
    IRRELEVANT(type = 1),

    /**
     * A review accumulating more than N upvotes on his latest review
     */
    POPULAR(type = 2)
}

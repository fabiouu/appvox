package dev.fabiou.appvox.core.review.constant

enum class AppReviewSortType(val sortType: Int) {
    RELEVANT(1),
    RECENT(2),
    RATING(3);

    companion object {
        fun fromValue(sortType: String) : AppReviewSortType {
            for (googlePlaySortType in values()) {
                if (googlePlaySortType.name == sortType) {
                    return googlePlaySortType
                }
            }
            return RELEVANT
        }
    }
}
package dev.fabiou.appvox.googleplay.review.constant

enum class GooglePlaySortType(val sortType: Int) {
    RELEVANT(sortType = 1),
    RECENT(sortType = 2),
    RATING(sortType = 3);

    companion object {
        fun fromValue(sortType: Int): GooglePlaySortType {
            for (googlePlaySortType in values()) {
                if (googlePlaySortType.sortType == sortType) {
                    return googlePlaySortType
                }
            }
            return RELEVANT
        }
    }
}

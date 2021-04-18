package dev.fabiou.appvox.review.googleplay.constant

enum class GooglePlaySortType(val sortType: Int) {
    RELEVANT(1),
    RECENT(2),
    RATING(3);

    companion object {
        fun fromValue(sortType: Int) : GooglePlaySortType {
            for (googlePlaySortType in GooglePlaySortType.values()) {
                if (googlePlaySortType.sortType == sortType) {
                    return googlePlaySortType
                }
            }
            return RELEVANT
        }
    }
}

package dev.fabiou.appvox.review.itunesrss.constant

enum class AppStoreSortType(val sortType: Int) {
    RELEVANT(1),
    RECENT(2);

    companion object {
        fun fromValue(sortType: String): AppStoreSortType {
            for (appStoreSortType in values()) {
                if (appStoreSortType.name == sortType) {
                    return appStoreSortType
                }
            }
            return RELEVANT
        }
    }
}

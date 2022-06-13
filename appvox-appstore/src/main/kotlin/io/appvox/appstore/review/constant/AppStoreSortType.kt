package io.appvox.appstore.review.constant

enum class AppStoreSortType(val sortType: Int) {
    RELEVANT(1),
    RECENT(2);

    companion object {
        fun fromValue(sortType: Int): AppStoreSortType {
            for (appStoreSortType in values()) {
                if (appStoreSortType.sortType == sortType) {
                    return appStoreSortType
                }
            }
            return RELEVANT
        }
    }
}

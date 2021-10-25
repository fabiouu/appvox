package io.appvox.appstore.review.constant

enum class ItunesRssSortType(val sortType: Int) {
    RELEVANT(1),
    RECENT(2);

    companion object {
        fun fromValue(sortType: Int): ItunesRssSortType {
            for (appStoreSortType in values()) {
                if (appStoreSortType.sortType == sortType) {
                    return appStoreSortType
                }
            }
            return RELEVANT
        }
    }
}

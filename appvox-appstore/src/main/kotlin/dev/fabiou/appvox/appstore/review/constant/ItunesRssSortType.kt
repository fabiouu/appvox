package dev.fabiou.appvox.appstore.review.constant

enum class ItunesRssSortType(val sortType: Int) {
    RELEVANT(1),
    RECENT(2);

    companion object {
        fun fromValue(sortType: String): ItunesRssSortType {
            for (appStoreSortType in values()) {
                if (appStoreSortType.name == sortType) {
                    return appStoreSortType
                }
            }
            return RELEVANT
        }
    }
}

package dev.fabiou.appvox.core.appstore.review.constant

enum class AppStoreSortType(val sortType: Int) {
    RELEVANT(1),
    RECENT(2);

    companion object {
        fun fromValue(sortType: String) : AppStoreSortType {
            for (appStoreSortType in AppStoreSortType.values()) {
                if (appStoreSortType.name == sortType) {
                    return appStoreSortType
                }
            }
            return RELEVANT
        }
    }
}
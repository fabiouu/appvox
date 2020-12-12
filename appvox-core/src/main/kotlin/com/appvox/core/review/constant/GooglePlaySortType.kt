package com.appvox.core.review.constant

enum class GooglePlaySortType(val sortType: Int) {
    RELEVANT(1),
    NEWEST(2),
    RATING(3);

    companion object {
        fun fromValue(sortType: String) : GooglePlaySortType {
            for (googlePlaySortType in GooglePlaySortType.values()) {
                if (googlePlaySortType.name == sortType) {
                    return googlePlaySortType
                }
            }
            return RELEVANT
        }
    }
}
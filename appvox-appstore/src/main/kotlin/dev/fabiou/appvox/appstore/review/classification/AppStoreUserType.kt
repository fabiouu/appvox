package dev.fabiou.appvox.appstore.review.classification

enum class AppStoreUserType(val type: Int) {
    /**
     * A detractor of your application (1->3 stars review)
     */
    DETRACTOR(type = 1),

    /**
     * A promoter of your application (4-5 stars review)
     */
    PROMOTER(type = 2)
}

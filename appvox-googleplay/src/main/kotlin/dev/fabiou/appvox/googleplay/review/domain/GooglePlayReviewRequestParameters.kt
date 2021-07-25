package dev.fabiou.appvox.googleplay.review.domain

import dev.fabiou.appvox.configuration.Constant
import dev.fabiou.appvox.googleplay.review.constant.GooglePlayLanguage
import dev.fabiou.appvox.googleplay.review.constant.GooglePlaySortType

data class GooglePlayReviewRequestParameters(
    val appId: String,
    val language: GooglePlayLanguage,
    val sortType: GooglePlaySortType,
    val rating: Int? = null,
    val fetchHistory: Boolean,
    val batchSize: Int,
    val deviceName: String? = null,
    val sid: String? = null,
    val bl: String? = null
) {
    private constructor(builder: Builder) : this(
        appId = builder.appId,
        language = builder.language,
        sortType = builder.sortType,
        rating = builder.rating,
        fetchHistory = builder.fetchHistory,
        deviceName = builder.deviceName,
        batchSize = builder.batchSize
    )

    class Builder {
        lateinit var appId: String
        var language: GooglePlayLanguage = GooglePlayLanguage.ENGLISH_US
        var sortType: GooglePlaySortType = GooglePlaySortType.RECENT
        var rating: Int? = null
        var fetchHistory: Boolean = false
        var deviceName: String? = null
        var batchSize: Int = Constant.DEFAULT_BATCH_SIZE

        fun build() = GooglePlayReviewRequestParameters(this)
    }
}

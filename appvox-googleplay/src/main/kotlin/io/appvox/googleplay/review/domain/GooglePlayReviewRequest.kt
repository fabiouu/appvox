package io.appvox.googleplay.review.domain

import io.appvox.core.configuration.Constant
import io.appvox.googleplay.review.constant.GooglePlayLanguage
import io.appvox.googleplay.review.constant.GooglePlaySortType

data class GooglePlayReviewRequest(
    val appId: String,
    val language: GooglePlayLanguage,
    val sortType: GooglePlaySortType,
    val rating: Int? = null,
    val fetchHistory: Boolean,
    val batchSize: Int,
    val deviceName: String? = null,
    val sid: String? = null,
    val bl: String? = null,
    val nextToken: String? = null
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

        fun build() = GooglePlayReviewRequest(this)
    }
}

package dev.fabiou.appvox.review.googleplay.constant

/**
 * The list of supported Google Play devices can be found at that address:
 * - https://storage.googleapis.com/play_public/supported_devices.html
 * Because there are thousands of devices in this list, only the top-selling smartphones are present in this enumeration
 *
 * Top-selling smartphones reference:
 * - https://en.wikipedia.org/wiki/List_of_best-selling_mobile_phones
 *
 * Phones are commonly known from users by their marketing name while GooglePlay uses their deviceName to filter.
 */
enum class GooglePlayDevice(val deviceName: String) {
    UNKNOWN(""),
    SAMSUNG_GALAXY_S7("herolte"),
    SAMSUNG_GALAXY_S7_ACTIVE("poseidonlteatt"),
    SAMSUNG_GALAXY_S7_EDGE("SC-02H"),
    SAMSUNG_GALAXY_S8("SC-02J"),
    SAMSUNG_GALAXY_S8_ACTIVE("cruiserlteatt"),
    SAMSUNG_GALAXY_S8_PLUS("SC-03J"),
    SAMSUNG_GALAXY_S9("SC-02K"),
    SAMSUNG_GALAXY_S9_PLUS("SC-03K");

    companion object {
        fun fromDeviceName(marketingName: String): GooglePlayDevice {
            for (googlePlayDevice in values()) {
                if (marketingName == googlePlayDevice.deviceName) {
                    return googlePlayDevice
                }
            }
            return UNKNOWN
        }
    }
}

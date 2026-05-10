package rahulstech.android.weathersnap.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Remote response model for current weather data.
 */
data class CurrentWeatherResponse(
    /**
     * The latitude of the location.
     * Stored as a [String] to preserve precision as required by the API.
     */
    @SerializedName("latitude")
    val latitude: String,
    /**
     * The longitude of the location.
     * Stored as a [String] to preserve precision as required by the API.
     */
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("current")
    val current: CurrentWeatherRemote
)

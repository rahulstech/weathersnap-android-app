package rahulstech.android.weathersnap.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Remote model representing a city search result.
 */
data class CitySearchRemote(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("country")
    val country: String,
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
    val longitude: String
)

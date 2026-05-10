package rahulstech.android.weathersnap.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Remote response model for city search, containing a list of [CitySearchRemote] results.
 */
data class CitySearchResponse(
    @SerializedName("results")
    val results: List<CitySearchRemote>? = null
)

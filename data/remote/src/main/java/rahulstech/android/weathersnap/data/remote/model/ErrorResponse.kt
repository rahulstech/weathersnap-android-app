package rahulstech.android.weathersnap.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Remote model representing an error response from the API.
 */
data class ErrorResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("reason")
    val reason: String
)

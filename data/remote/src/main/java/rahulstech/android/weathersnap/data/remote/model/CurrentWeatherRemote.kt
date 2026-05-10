package rahulstech.android.weathersnap.data.remote.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

/**
 * Remote model representing the current weather data.
 */
data class CurrentWeatherRemote(
    @SerializedName("time")
    @JsonAdapter(LocalDateTimeAdapter::class)
    val time: LocalDateTime,
    @SerializedName("temperature_2m")
    val temperature: Double,
    @SerializedName("wind_speed_10m")
    val windSpeed: Double,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("surface_pressure")
    val surfacePressure: Double,
    @SerializedName("relative_humidity_2m")
    val humidity: Int
)

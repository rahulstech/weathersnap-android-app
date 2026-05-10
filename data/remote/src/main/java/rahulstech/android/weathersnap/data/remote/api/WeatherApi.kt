package rahulstech.android.weathersnap.data.remote.api

import rahulstech.android.weathersnap.data.remote.model.CurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for weather services.
 */
interface WeatherApi {

    /**
     * Get current weather for a specific location.
     *
     * @param latitude The latitude of the location as a string.
     * @param longitude The longitude of the location as a string.
     * @param current Fields to include in the current weather data. Defaults to required fields.
     * @return A [CurrentWeatherResponse] containing weather data.
     */
    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("current") current: String = "temperature_2m,wind_speed_10m,weather_code,surface_pressure"
    ): CurrentWeatherResponse
}

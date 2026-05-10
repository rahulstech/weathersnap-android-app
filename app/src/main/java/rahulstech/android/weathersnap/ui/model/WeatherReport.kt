package rahulstech.android.weathersnap.ui.model

import rahulstech.android.weathersnap.data.remote.model.CitySearchRemote
import rahulstech.android.weathersnap.data.remote.model.CurrentWeatherResponse
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

data class WeatherReport(
    val id: Long,
    val cityName: String,
    val country: String,
    val temperature: Double,
    val windSpeed: Double,
    val weatherCode: Int,
    val surfacePressure: Double,
    val humidity: Int,
    val time: LocalDateTime,
    val latitude: String,
    val longitude: String
) {
    companion object {
        fun fromResponse(city: CitySearchRemote, response: CurrentWeatherResponse): WeatherReport {
            val current = response.current
            
            // Convert API local time to device local time
            val apiLocalTime = current.time
            val zoneOffset = ZoneOffset.ofTotalSeconds(response.utcOffsetSeconds)
            val instant = apiLocalTime.toInstant(zoneOffset)
            val deviceLocalTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()

            return WeatherReport(
                id = city.id,
                cityName = city.name,
                country = city.country,
                temperature = current.temperature,
                windSpeed = current.windSpeed,
                weatherCode = current.weatherCode,
                surfacePressure = current.surfacePressure,
                humidity = current.humidity,
                time = deviceLocalTime,
                latitude = city.latitude,
                longitude = city.longitude
            )
        }
    }
}

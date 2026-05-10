package rahulstech.android.weathersnap.ui.model

import rahulstech.android.weathersnap.data.remote.model.CitySearchRemote
import rahulstech.android.weathersnap.data.remote.model.CurrentWeatherRemote
import rahulstech.android.weathersnap.data.remote.model.CurrentWeatherResponse
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

data class WeatherReport(
    val city: CitySearchRemote,
    val weather: CurrentWeatherRemote,
    val deviceTime: LocalDateTime
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
                city = city,
                weather = current,
                deviceTime = deviceLocalTime
            )
        }
    }
}

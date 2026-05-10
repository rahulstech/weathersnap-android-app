package rahulstech.android.weathersnap.data.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteHiltModule {

    private const val CITY_BASE_URL = "https://geocoding-api.open-meteo.com/"
    private const val WEATHER_BASE_URL = "https://api.open-meteo.com/"

    @Provides
    @Singleton
    fun provideWeatherClient(): WeatherClient {
        return WeatherClient(
            weatherBaseUrl = WEATHER_BASE_URL,
            cityBaseUrl = CITY_BASE_URL
        )
    }
}

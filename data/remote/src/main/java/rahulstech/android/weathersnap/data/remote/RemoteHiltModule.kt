package rahulstech.android.weathersnap.data.remote

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteHiltModule {

    private const val CITY_BASE_URL = "https://geocoding-api.open-meteo.com/"
    private const val WEATHER_BASE_URL = "https://api.open-meteo.com/"

    @Provides
    @Singleton
    fun provideWeatherClient(@ApplicationContext context: Context): WeatherClient {
        return WeatherClient(
            weatherBaseUrl = WEATHER_BASE_URL,
            cityBaseUrl = CITY_BASE_URL,
            cacheDir = File(context.cacheDir, "http_cache")
        )
    }
}

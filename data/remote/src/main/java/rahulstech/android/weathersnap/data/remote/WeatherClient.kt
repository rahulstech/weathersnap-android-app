package rahulstech.android.weathersnap.data.remote

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import rahulstech.android.weathersnap.data.remote.api.CityApi
import rahulstech.android.weathersnap.data.remote.api.WeatherApi
import rahulstech.android.weathersnap.data.remote.model.LocalDateTimeAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.time.LocalDateTime

/**
 * Client for accessing weather and city APIs.
 *
 * @property weatherBaseUrl The base URL for the weather API.
 * @property cityBaseUrl The base URL for the city search API.
 * @property cacheDir The directory where the HTTP cache will be stored.
 */
class WeatherClient(
    private val weatherBaseUrl: String,
    private val cityBaseUrl: String,
    private val cacheDir: File
) {

    companion object {
        private const val TAG = "WeatherClient"
        private const val CACHE_SIZE = 10L * 1024 * 1024 // 10 MB
    }

    private val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()
    }

    private val loggingInterceptor = Interceptor { chain ->
        val request = chain.request()
        val url = request.url()
        Log.d(TAG, "Request: BaseURL: ${url.scheme()}://${url.host()}, Path: ${url.encodedPath()}, Queries: ${url.query()}")
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .cache(Cache(cacheDir, CACHE_SIZE))
        .addInterceptor(loggingInterceptor)
        .build()

    /**
     * Lazy-initialized [WeatherApi] instance.
     */
    val weatherApi: WeatherApi by lazy {
        createRetrofitClient(weatherBaseUrl).create(WeatherApi::class.java)
    }

    /**
     * Lazy-initialized [CityApi] instance.
     */
    val cityApi: CityApi by lazy {
        createRetrofitClient(cityBaseUrl).create(CityApi::class.java)
    }

    private fun createRetrofitClient(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}

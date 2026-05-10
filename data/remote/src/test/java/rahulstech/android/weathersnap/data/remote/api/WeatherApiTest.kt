package rahulstech.android.weathersnap.data.remote.api

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import rahulstech.android.weathersnap.data.remote.WeatherClient
import java.time.LocalDateTime

class WeatherApiTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var weatherClient: WeatherClient

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val baseUrl = mockWebServer.url("/").toString()
        weatherClient = WeatherClient(baseUrl, baseUrl, tempFolder.newFolder("http_cache"))
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetCurrentWeatherSuccess() = runBlocking {
        val jsonResponse = """
            {
              "latitude": 51.51147,
              "longitude": -0.13078308,
              "generationtime_ms": 0.101447105407715,
              "utc_offset_seconds": 0,
              "timezone": "GMT",
              "timezone_abbreviation": "GMT",
              "elevation": 23,
              "current_units": {
                "time": "iso8601",
                "interval": "seconds",
                "temperature_2m": "°C",
                "wind_speed_10m": "km/h",
                "weather_code": "wmo code",
                "surface_pressure": "hPa"
              },
              "current": {
                "time": "2026-05-10T06:45",
                "interval": 900,
                "temperature_2m": 10.3,
                "wind_speed_10m": 17.6,
                "weather_code": 3,
                "surface_pressure": 1015.2
              }
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))

        val response = weatherClient.weatherApi.getCurrentWeather("51.51147", "-0.13078308")

        assertEquals("51.51147", response.latitude)
        assertEquals("-0.13078308", response.longitude)
        assertEquals(0, response.utcOffsetSeconds)
        assertEquals("GMT", response.timezone)

        assertNotNull(response.current)
        val current = response.current
        assertEquals(LocalDateTime.of(2026, 5, 10, 6, 45), current.time)
        assertEquals(10.3, current.temperature, 0.001)
        assertEquals(17.6, current.windSpeed, 0.001)
        assertEquals(3, current.weatherCode)
        assertEquals(1015.2, current.surfacePressure, 0.001)

        val recordedRequest = mockWebServer.takeRequest()
        val path = recordedRequest.path
        assertNotNull(path)
        assert(path!!.contains("/v1/forecast"))
        assert(path.contains("latitude=51.51147"))
        assert(path.contains("longitude=-0.13078308"))
        // Retrofit encodes the comma as %2C
        assert(path.contains("current=temperature_2m%2Cwind_speed_10m%2Cweather_code%2Csurface_pressure"))
    }
}

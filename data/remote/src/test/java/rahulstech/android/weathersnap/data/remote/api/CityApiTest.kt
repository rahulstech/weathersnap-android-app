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

class CityApiTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var weatherClient: WeatherClient

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val baseUrl = mockWebServer.url("/").toString()
        // Both URLs pointing to mock server for simplicity in this test
        weatherClient = WeatherClient(baseUrl, baseUrl, tempFolder.newFolder("http_cache"))
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testSearchCitiesSuccess() = runBlocking {
        val jsonResponse = """
            {
              "results": [
                {
                  "id": 2643743,
                  "name": "London",
                  "latitude": 51.50853,
                  "longitude": -0.12574,
                  "elevation": 25,
                  "feature_code": "PPLC",
                  "country_code": "GB",
                  "admin1_id": 6269131,
                  "admin2_id": 2648110,
                  "timezone": "Europe/London",
                  "population": 8961989,
                  "country_id": 2635167,
                  "country": "United Kingdom",
                  "admin1": "England",
                  "admin2": "Greater London"
                },
                {
                  "id": 6058560,
                  "name": "London",
                  "latitude": 42.98339,
                  "longitude": -81.23304,
                  "elevation": 252,
                  "feature_code": "PPL",
                  "country_code": "CA",
                  "admin1_id": 6093943,
                  "admin2_id": 6073256,
                  "timezone": "America/Toronto",
                  "population": 422324,
                  "country_id": 6251999,
                  "country": "Canada",
                  "admin1": "Ontario",
                  "admin2": "Middlesex County"
                }
              ],
              "generationtime_ms": 0.7841587
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))

        val response = weatherClient.cityApi.searchCities("London")

        assertNotNull(response.results)
        assertEquals(2, response.results?.size)

        val firstCity = response.results!![0]
        assertEquals(2643743L, firstCity.id)
        assertEquals("London", firstCity.name)
        assertEquals("United Kingdom", firstCity.country)
        assertEquals("51.50853", firstCity.latitude)
        assertEquals("-0.12574", firstCity.longitude)

        val secondCity = response.results!![1]
        assertEquals(6058560L, secondCity.id)
        assertEquals("London", secondCity.name)
        assertEquals("Canada", secondCity.country)
        assertEquals("42.98339", secondCity.latitude)
        assertEquals("-81.23304", secondCity.longitude)

        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("/v1/search?name=London", recordedRequest.path)
    }
}

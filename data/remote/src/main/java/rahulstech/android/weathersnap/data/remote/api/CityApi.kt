package rahulstech.android.weathersnap.data.remote.api

import rahulstech.android.weathersnap.data.remote.model.CitySearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for city-related services.
 */
interface CityApi {

    /**
     * Search for cities by name.
     *
     * @param name The name of the city to search for.
     * @return A [CitySearchResponse] containing search results.
     */
    @GET("v1/search")
    suspend fun searchCities(
        @Query("name") name: String
    ): CitySearchResponse
}

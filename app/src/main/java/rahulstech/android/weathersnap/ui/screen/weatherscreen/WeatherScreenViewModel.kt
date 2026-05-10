package rahulstech.android.weathersnap.ui.screen.weatherscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rahulstech.android.weathersnap.data.remote.WeatherClient
import rahulstech.android.weathersnap.data.remote.model.CitySearchRemote
import rahulstech.android.weathersnap.ui.model.WeatherReport
import rahulstech.android.weathersnap.ui.util.Resource
import javax.inject.Inject

@HiltViewModel
class WeatherScreenViewModel @Inject constructor(
    private val weatherClient: WeatherClient
) : ViewModel() {

    var citySearchResource by mutableStateOf<Resource<List<CitySearchRemote>>>(Resource.Idle)
        private set

    var weatherResource by mutableStateOf<Resource<WeatherReport>>(Resource.Idle)
        private set

    private var searchJob: Job? = null

    fun searchCities(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500) // Debounce
            citySearchResource = Resource.Loading
            try {
                val response = weatherClient.cityApi.searchCities(query)
                citySearchResource = Resource.Success(response.results ?: emptyList())
            } catch (e: Exception) {
                citySearchResource = Resource.Error(e)
            }
        }
    }

    fun fetchWeather(city: CitySearchRemote) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherResource = Resource.Loading
            try {
                val response = weatherClient.weatherApi.getCurrentWeather(
                    latitude = city.latitude,
                    longitude = city.longitude
                )
                val report = WeatherReport.fromResponse(city, response)
                weatherResource = Resource.Success(report)
            } catch (e: Exception) {
                weatherResource = Resource.Error(e)
            }
        }
    }
}

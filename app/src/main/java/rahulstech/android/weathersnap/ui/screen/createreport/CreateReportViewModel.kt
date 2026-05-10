package rahulstech.android.weathersnap.ui.screen.createreport

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rahulstech.android.weathersnap.data.remote.WeatherClient
import rahulstech.android.weathersnap.data.remote.model.CitySearchRemote
import rahulstech.android.weathersnap.ui.model.ImageCaptureResult
import rahulstech.android.weathersnap.ui.model.WeatherReport
import rahulstech.android.weathersnap.ui.util.Resource
import javax.inject.Inject

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    private val weatherClient: WeatherClient
) : ViewModel() {

    var weatherResource by mutableStateOf<Resource<WeatherReport>>(Resource.Idle)
        private set

    var city by mutableStateOf<CitySearchRemote?>(null)
    var imageCaptureResult by mutableStateOf<ImageCaptureResult?>(null)

    init {
        fetchWeather()
    }

    fun fetchWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            if (city == null) {
                weatherResource = Resource.Error(Exception("city is null"))
                return@launch
            }
            weatherResource = Resource.Loading
            val city1 = city!!
            try {
                val response = weatherClient.weatherApi.getCurrentWeather(
                    latitude = city1.latitude,
                    longitude = city1.latitude
                )
                val report = WeatherReport.fromResponse(city1, response)
                weatherResource = Resource.Success(report)
            } catch (e: Exception) {
                weatherResource = Resource.Error(e)
            }
        }
    }
}

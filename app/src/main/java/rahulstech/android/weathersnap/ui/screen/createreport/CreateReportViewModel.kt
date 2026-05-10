package rahulstech.android.weathersnap.ui.screen.createreport

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rahulstech.android.weathersnap.data.local.dao.SavedWeatherReportDao
import rahulstech.android.weathersnap.data.remote.WeatherClient
import rahulstech.android.weathersnap.data.remote.model.CitySearchRemote
import rahulstech.android.weathersnap.ui.model.ImageCaptureResult
import rahulstech.android.weathersnap.ui.model.WeatherReport
import rahulstech.android.weathersnap.ui.util.toEntity
import javax.inject.Inject

data class CreateReportUIState(
    val isWeatherLoading: Boolean = false,
    val weatherReport: WeatherReport? = null,
    val weatherLoadError: String? = null,
    val imageCaptureResult: ImageCaptureResult? = null,
    val note: String? = null,
    val isSaving: Boolean = false
)

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    private val weatherClient: WeatherClient,
    private val savedWeatherReportDao: SavedWeatherReportDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReportUIState())
    val uiState: StateFlow<CreateReportUIState> = _uiState.asStateFlow()

    private var _city by mutableStateOf<CitySearchRemote?>(null)
    
    var city: CitySearchRemote?
        get() = _city
        set(value) {
            _city = value
            if (value != null) {
                fetchWeather()
            }
        }

    private val _saveSuccess = MutableSharedFlow<Boolean>()
    val saveSuccess = _saveSuccess.asSharedFlow()

    fun fetchWeather() {
        val currentCity = _city ?: return
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isWeatherLoading = true, weatherLoadError = null) }
            try {
                val response = weatherClient.weatherApi.getCurrentWeather(
                    latitude = currentCity.latitude,
                    longitude = currentCity.longitude
                )
                val report = WeatherReport.fromResponse(currentCity, response)
                _uiState.update { it.copy(isWeatherLoading = false, weatherReport = report) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isWeatherLoading = false, weatherLoadError = e.message ?: "Unknown error") }
            }
        }
    }

    fun onNoteChange(newNote: String) {
        _uiState.update { it.copy(note = newNote) }
    }

    fun onImageCaptured(result: ImageCaptureResult) {
        _uiState.update { it.copy(imageCaptureResult = result) }
    }

    fun saveReport() {
        val state = _uiState.value
        val weather = state.weatherReport ?: return
        val imageResult = state.imageCaptureResult ?: return

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val entity = imageResult.toEntity(weather, state.note?.ifBlank { null })
                savedWeatherReportDao.create(entity)
                
                // Reset everything on success
                _city = null
                _uiState.value = CreateReportUIState()
                
                _saveSuccess.emit(true)
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, weatherLoadError = e.message) }
            }
        }
    }
}

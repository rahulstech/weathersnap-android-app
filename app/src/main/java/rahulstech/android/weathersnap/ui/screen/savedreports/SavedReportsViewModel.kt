package rahulstech.android.weathersnap.ui.screen.savedreports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import rahulstech.android.weathersnap.data.local.dao.SavedWeatherReportDao
import rahulstech.android.weathersnap.data.local.entity.SavedWeatherReportEntity
import javax.inject.Inject

@HiltViewModel
class SavedReportsViewModel @Inject constructor(
    private val savedWeatherReportDao: SavedWeatherReportDao
) : ViewModel() {

    val savedReports: StateFlow<List<SavedWeatherReportEntity>> = savedWeatherReportDao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
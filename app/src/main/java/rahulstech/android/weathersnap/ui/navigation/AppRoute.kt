package rahulstech.android.weathersnap.ui.navigation

import rahulstech.android.weathersnap.data.remote.model.CitySearchRemote

sealed class AppRoute(val name: String) {
    object WeatherSearch : AppRoute("weathersearch")
    object SavedReports : AppRoute("savereport")
    object Camera : AppRoute("camera")
    data class CreateReport(val city: CitySearchRemote) : AppRoute("createreport") {
        companion object {
            const val ROUTE_PATTERN = "createreport"
        }
    }
}

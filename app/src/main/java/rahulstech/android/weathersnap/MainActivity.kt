package rahulstech.android.weathersnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import rahulstech.android.weathersnap.ui.navigation.AppRoute
import rahulstech.android.weathersnap.ui.screen.createreport.CameraRoute
import rahulstech.android.weathersnap.ui.screen.createreport.CreateReportRoute
import rahulstech.android.weathersnap.ui.screen.createreport.CreateReportViewModel
import rahulstech.android.weathersnap.ui.screen.savedreports.SavedReportsRoute
import rahulstech.android.weathersnap.ui.screen.weatherscreen.WeatherRoute
import rahulstech.android.weathersnap.ui.theme.WeatherSnapTheme
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val createReportViewModel by viewModels<CreateReportViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherSnapTheme {
                val navController = rememberNavController()

                val onNavigate: (AppRoute) -> Unit = { route ->
                    when (route) {
                        is AppRoute.CreateReport -> {
                            createReportViewModel.city = route.city
                            navController.navigate("createreport")
                        }
                        else -> {
                            navController.navigate(route.name)
                        }
                    }
                }

                val onExit: () -> Unit = {
                    navController.popBackStack()
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = AppRoute.WeatherSearch.name
                        ) {
                            composable(AppRoute.WeatherSearch.name) {
                                WeatherRoute(
                                    onNavigate = onNavigate
                                )
                            }
                            composable(AppRoute.SavedReports.name) {
                                SavedReportsRoute(
                                    onExit = onExit
                                )
                            }
                            composable(AppRoute.Camera.name) {
                                CameraRoute(
                                    viewModel = createReportViewModel,
                                    onExit = onExit,
                                )
                            }
                            composable(
                                route = AppRoute.CreateReport.ROUTE_PATTERN
                            ) {
                                CreateReportRoute(
                                    viewModel = createReportViewModel,
                                    onNavigate = onNavigate,
                                    onExit = onExit
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

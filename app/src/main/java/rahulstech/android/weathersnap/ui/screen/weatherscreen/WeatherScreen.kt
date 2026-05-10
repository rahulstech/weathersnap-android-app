package rahulstech.android.weathersnap.ui.screen.weatherscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import rahulstech.android.weathersnap.ui.component.WeatherInfoCard
import rahulstech.android.weathersnap.ui.component.WeatherInfoShimmerCard
import rahulstech.android.weathersnap.ui.component.WeatherSnapHeader
import rahulstech.android.weathersnap.ui.model.WeatherReport
import rahulstech.android.weathersnap.ui.theme.WeatherSnapTheme
import rahulstech.android.weathersnap.ui.util.Resource

import rahulstech.android.weathersnap.data.remote.model.CitySearchRemote
import rahulstech.android.weathersnap.ui.navigation.AppRoute

@Composable
fun WeatherRoute(
    onNavigate: (AppRoute) -> Unit,
    viewModel: WeatherScreenViewModel = hiltViewModel()
) {
    WeatherScreen(
        citySearchResource = viewModel.citySearchResource,
        weatherResource = viewModel.weatherResource,
        onSearchCities = { viewModel.searchCities(it) },
        onFetchWeather = { viewModel.fetchWeather(it) },
        onNavigate = onNavigate,
    )
}

@Composable
fun WeatherScreen(
    citySearchResource: Resource<List<CitySearchRemote>>,
    weatherResource: Resource<WeatherReport>,
    onSearchCities: (String) -> Unit,
    onFetchWeather: (CitySearchRemote) -> Unit,
    onNavigate: (AppRoute) -> Unit,
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WeatherSnapHeader(
            title = "WeatherSnap",
            subtitle = "Live weather reports with camera evidence",
            actionLabel = "Reports",
            onAction = { onNavigate(AppRoute.SavedReports) }
        )

        CitySearchBar(
            searchCity = onSearchCities,
            citySearchResource = citySearchResource,
            fetchCurrentWeather = onFetchWeather,
        )

        WeatherInfoSection(
            weatherResource = weatherResource,
            onNavigate = onNavigate
        )
    }
}

@Composable
fun CitySearchBar(
    searchCity: (String) -> Unit,
    citySearchResource: Resource<List<CitySearchRemote>>,
    fetchCurrentWeather: (CitySearchRemote) -> Unit,
) {
    var selectedCity by remember { mutableStateOf<CitySearchRemote?>(null) }
    var query by remember { mutableStateOf("") }
    var showSuggestions by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { newValue ->
                query = newValue
                selectedCity = null

                if (newValue.length > 2) {
                    showSuggestions = true
                    searchCity(newValue)
                } else {
                    showSuggestions = false
                }
            },
            label = { Text("City") },
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.extraSmall,
            supportingText = {
                Text("Enter more than 2 letters for suggestions")
            },
            singleLine = true
        )

        Button(
            onClick = {
                selectedCity?.let { fetchCurrentWeather(it) }
            },
            modifier = Modifier.padding(top = 8.dp),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text("Search")
        }
    }

    if (showSuggestions) {
        SearchResultSection(
            citySearchResource = citySearchResource,
            onCitySelected = {
                selectedCity = it

                query = "${it.name}, ${it.country}"

                showSuggestions = false
            }
        )
    }
}

@Composable
fun SearchResultSection(
    citySearchResource: Resource<List<CitySearchRemote>>,
    onCitySelected: (CitySearchRemote) -> Unit
) {
    when(citySearchResource) {
        is Resource.Loading -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Text(
                    text = "Fetching cities",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        is Resource.Success -> {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    citySearchResource.data.forEach { prediction ->
                        TextButton(
                            onClick = { onCitySelected(prediction) },
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            Text(
                                text = "${prediction.name}, ${prediction.country}",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
        else -> {}
    }
}

@Composable
fun WeatherInfoSection(
    weatherResource: Resource<WeatherReport>,
    onNavigate: (AppRoute) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (weatherResource is Resource.Idle) {
                EmptyWeatherCard()
            } else {
                WeatherInfoCard(weatherResource = weatherResource)

                if (weatherResource is Resource.Success) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Report readiness",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Camera and Room DB enabled",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        onClick = {
                            val report = weatherResource.data
                            onNavigate(
                                AppRoute.CreateReport(
                                    city = CitySearchRemote(id=0,name=report.cityName, country = report.country, report.latitude, report.longitude)
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create Report", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyWeatherCard() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceContainer,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    ),
                    shape = MaterialTheme.shapes.medium
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Search. Capture. Save.",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "No weather loaded",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Enter more than 2 letters, choose a city, then search.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    WeatherSnapTheme {
        WeatherScreen(
            citySearchResource = Resource.Idle,
            weatherResource = Resource.Idle,
            onSearchCities = {},
            onFetchWeather = {},
            onNavigate = {},
        )
    }
}
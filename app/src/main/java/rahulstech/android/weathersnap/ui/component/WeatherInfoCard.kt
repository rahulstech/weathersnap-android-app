package rahulstech.android.weathersnap.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import rahulstech.android.weathersnap.ui.model.WeatherReport
import rahulstech.android.weathersnap.ui.util.Resource
import rahulstech.android.weathersnap.ui.util.getWeatherCondition
import rahulstech.android.weathersnap.ui.util.shimmer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import rahulstech.android.weathersnap.data.remote.model.CitySearchRemote
import rahulstech.android.weathersnap.data.remote.model.CurrentWeatherRemote
import rahulstech.android.weathersnap.ui.theme.WeatherSnapTheme
import java.time.LocalDateTime

@Composable
fun WeatherInfoCard(
    weatherResource: Resource<WeatherReport>,
    modifier: Modifier = Modifier
) {
    when (weatherResource) {
        is Resource.Loading -> {
            WeatherInfoShimmerCard(modifier = modifier)
        }
        is Resource.Success -> {
            WeatherInfoSuccessCard(report = weatherResource.data, modifier = modifier)
        }
        is Resource.Error -> {
            WeatherInfoErrorCard(
                message = weatherResource.cause.message ?: "Unknown error",
                modifier = modifier
            )
        }
        Resource.Idle -> {
            // Can be handled by caller or shown as empty
        }
    }
}

@Composable
fun WeatherInfoSuccessCard(
    report: WeatherReport,
    modifier: Modifier = Modifier
) {
    val city = report.city
    val weather = report.weather
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${city.name}, ${city.country}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = getWeatherCondition(weather.weatherCode),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = "${weather.temperature}°C",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoBox(
                label = "Humidity",
                text = "${weather.humidity}%",
                textColor = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            InfoBox(
                label = "Wind",
                text = "${weather.windSpeed} km/h",
                textColor = Color(0xFF42A5F5),
                modifier = Modifier.weight(1f)
            )
            InfoBox(
                label = "Pressure",
                text = "${weather.surfacePressure}",
                textColor = Color(0xFFFFB74D),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun WeatherInfoErrorCard(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Error loading weather",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WeatherInfoShimmerCard(
    modifier: Modifier = Modifier
) {
    val shimmerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
    val highlightColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(36.dp),
                strokeWidth = 3.dp
            )
            Column {
                Text(
                    text = "Loading weather...",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Fetching city name and current condition",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .clip(MaterialTheme.shapes.small)
                        .shimmer(baseColor = shimmerColor, highlightColor = highlightColor)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherInfoCardPreview() {
    val previewData = WeatherReport(
        city = CitySearchRemote(
            id = 1,
            name = "San Francisco",
            country = "USA",
            latitude = "37.7749",
            longitude = "-122.4194"
        ),
        weather = CurrentWeatherRemote(
            time = LocalDateTime.now(),
            temperature = 18.5,
            windSpeed = 12.0,
            weatherCode = 1,
            surfacePressure = 1012.0,
            humidity = 65
        ),
        deviceTime = LocalDateTime.now()
    )
    WeatherSnapTheme {
        WeatherInfoCard(
            weatherResource = Resource.Success(previewData),
            modifier = Modifier.padding(16.dp)
        )
    }
}

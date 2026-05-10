package rahulstech.android.weathersnap.ui.screen.createreport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rahulstech.android.weathersnap.ui.component.WeatherInfoCard
import rahulstech.android.weathersnap.ui.component.WeatherSnapHeader
import rahulstech.android.weathersnap.ui.model.WeatherReport
import rahulstech.android.weathersnap.ui.theme.WeatherSnapTheme
import java.time.LocalDateTime

@Composable
fun CreateReportScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        WeatherSnapHeader(
            title = "Create Report",
            subtitle = "Capture, compress, annotate",
            actionLabel = "Back",
            onAction = {}
        )

        WeatherInfoSection()

        PhotoCaptureCard()

        NotesCard()

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text("Save Report", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
fun WeatherInfoSection() {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
    ) {
        val dummyReport = WeatherReport(
            id = 1,
            cityName = "Placeholder City",
            country = "Country",
            temperature = 20.0,
            windSpeed = 5.0,
            weatherCode = 0,
            surfacePressure = 1013.25,
            humidity = 50,
            time = LocalDateTime.now()
        )
        WeatherInfoCard(
            report = dummyReport,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun PhotoCaptureCard() {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
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
                    text = "Photo preview",
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text("Capture Photo")
            }
        }
    }
}

@Composable
fun NotesCard() {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Field Notes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Notes", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = MaterialTheme.shapes.small,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateReportScreenPreview() {
    WeatherSnapTheme {
        CreateReportScreen()
    }
}

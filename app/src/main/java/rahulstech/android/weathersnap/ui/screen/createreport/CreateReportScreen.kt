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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import rahulstech.android.weathersnap.ui.component.InfoBox
import rahulstech.android.weathersnap.ui.component.WeatherInfoCard
import rahulstech.android.weathersnap.ui.component.WeatherSnapHeader
import rahulstech.android.weathersnap.ui.model.ImageCaptureResult
import rahulstech.android.weathersnap.ui.model.WeatherReport
import rahulstech.android.weathersnap.ui.navigation.AppRoute
import rahulstech.android.weathersnap.ui.theme.WeatherSnapTheme
import rahulstech.android.weathersnap.ui.util.Resource
import java.time.LocalDateTime

@Composable
fun CreateReportRoute(
    onNavigate: (AppRoute) -> Unit,
    onExit: () -> Unit,
    viewModel: CreateReportViewModel
) {
    val imageCaptureResult = viewModel.imageCaptureResult
    android.util.Log.d("CreateReportScreen", "CreateReportRoute: imageCaptureResult=$imageCaptureResult")
    CreateReportScreen(
        weatherResource = viewModel.weatherResource,
        imageCaptureResult = imageCaptureResult,
        onNavigate = onNavigate,
        onExit = onExit
    )
}

@Composable
fun CreateReportScreen(
    weatherResource: Resource<WeatherReport>,
    imageCaptureResult: ImageCaptureResult?,
    onNavigate: (AppRoute) -> Unit,
    onExit: () -> Unit
) {
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
            onAction = onExit
        )

        WeatherInfoSection(weatherResource = weatherResource)

        PhotoCaptureCard(
            imageCaptureResult = imageCaptureResult,
            onCaptureClick = { onNavigate(AppRoute.Camera) }
        )

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
fun WeatherInfoSection(weatherResource: Resource<WeatherReport>) {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
    ) {
        WeatherInfoCard(
            weatherResource = weatherResource,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun PhotoCaptureCard(
    imageCaptureResult: ImageCaptureResult?,
    onCaptureClick: () -> Unit
) {
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
                if (imageCaptureResult != null) {

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageCaptureResult.filePath)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Photo preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            InfoBox(
                                label = "Original",
                                text = "${imageCaptureResult.rawSize / 1024} KB",
                                textColor = Color(0xFFFFB74D),
                                modifier = Modifier.weight(1f)
                            )

                            InfoBox(
                                label = "Compressed",
                                text = "${imageCaptureResult.compressSize / 1024} KB",
                                textColor = Color(0xFF81C784),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                } else {
                    Text(
                        text = "Photo preview",
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Button(
                onClick = onCaptureClick,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(if (imageCaptureResult == null) "Capture Photo" else "Retake Photo")
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

    val previewData = WeatherReport(
        id = 1,
        cityName = "San Francisco",
        country = "USA",
        temperature = 18.5,
        windSpeed = 12.0,
        weatherCode = 1,
        surfacePressure = 1012.0,
        humidity = 65,
        time = LocalDateTime.now(),
        latitude = "37.7749",
        longitude = "-122.4194"
    )
    WeatherSnapTheme {
        CreateReportScreen(
            weatherResource = Resource.Success(previewData),
            imageCaptureResult = ImageCaptureResult(
                filePath = "",
                rawSize = 1024 * 100,
                compressSize = 1024 * 40
            ),
            onNavigate = {},
            onExit = {}
        )
    }
}

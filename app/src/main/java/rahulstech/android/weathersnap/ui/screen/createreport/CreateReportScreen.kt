package rahulstech.android.weathersnap.ui.screen.createreport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.saveSuccess.collect { success ->
            if (success) {
                // pop this scree so that we don't came back from SaveReports screen on back
                onExit()
                
                // now navigate to SavedReports screen
                onNavigate(AppRoute.SavedReports)
            }
        }
    }

    CreateReportScreen(
        uiState = uiState,
        onNoteChange = viewModel::onNoteChange,
        onSaveReport = viewModel::saveReport,
        onNavigate = onNavigate,
        onExit = onExit
    )
}

@Composable
fun CreateReportScreen(
    uiState: CreateReportUIState,
    onNoteChange: (String) -> Unit,
    onSaveReport: () -> Unit,
    onNavigate: (AppRoute) -> Unit,
    onExit: () -> Unit
) {
    val weatherResource = remember(uiState.isWeatherLoading, uiState.weatherReport, uiState.weatherLoadError) {
        when {
            uiState.isWeatherLoading -> Resource.Loading
            uiState.weatherLoadError != null -> Resource.Error(Exception(uiState.weatherLoadError))
            uiState.weatherReport != null -> Resource.Success(uiState.weatherReport)
            else -> Resource.Idle
        }
    }

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
            imageCaptureResult = uiState.imageCaptureResult,
            onCaptureClick = { onNavigate(AppRoute.Camera) }
        )

        NotesCard(
            note = uiState.note ?: "",
            onNoteChange = onNoteChange
        )

        Button(
            onClick = onSaveReport,
            enabled = uiState.imageCaptureResult != null && uiState.weatherReport != null && !uiState.isSaving,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Save Report", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
            }
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
                    .heightIn(min = 200.dp)
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
fun NotesCard(
    note: String,
    onNoteChange: (String) -> Unit
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Field Notes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = note,
                onValueChange = onNoteChange,
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
        city = rahulstech.android.weathersnap.data.remote.model.CitySearchRemote(
            id = 1,
            name = "San Francisco",
            country = "USA",
            latitude = "37.7749",
            longitude = "-122.4194"
        ),
        weather = rahulstech.android.weathersnap.data.remote.model.CurrentWeatherRemote(
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
        CreateReportScreen(
            uiState = CreateReportUIState(
                weatherReport = previewData,
                imageCaptureResult = ImageCaptureResult(
                    filePath = "",
                    rawSize = 1024 * 100,
                    compressSize = 1024 * 40
                )
            ),
            onNoteChange = {},
            onSaveReport = {},
            onNavigate = {},
            onExit = {}
        )
    }
}

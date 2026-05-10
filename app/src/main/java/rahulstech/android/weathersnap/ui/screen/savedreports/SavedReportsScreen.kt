package rahulstech.android.weathersnap.ui.screen.savedreports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import androidx.compose.ui.text.style.TextAlign
import rahulstech.android.weathersnap.data.local.entity.SavedWeatherReportEntity
import rahulstech.android.weathersnap.ui.component.InfoBox
import rahulstech.android.weathersnap.ui.component.WeatherSnapHeader
import rahulstech.android.weathersnap.ui.theme.WeatherSnapTheme
import rahulstech.android.weathersnap.ui.util.getWeatherCondition
import java.time.format.DateTimeFormatter


@Composable
fun SavedReportsRoute(
    onExit: () -> Unit,
    viewModel: SavedReportsViewModel = hiltViewModel()
) {
    val reports by viewModel.savedReports.collectAsStateWithLifecycle()
    SavedReportsScreen(
        onExit = onExit,
        reports = reports
    )
}
@Composable
fun SavedReportsScreen(
    reports: List<SavedWeatherReportEntity>,
    onExit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WeatherSnapHeader(
            title = "Saved Reports",
            subtitle = "${reports.size} report stored locally",
            actionLabel = "Back",
            onAction = onExit,
        )

        if (reports.isEmpty()) {
            EmptyReportsView(Modifier.weight(1f))
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(items = reports, key = { it.id }) { report ->
                    ReportCard(report = report)
                }
            }
        }
    }
}

@Composable
fun EmptyReportsView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Placeholder for the "image" mentioned by user
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
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Create and save weather reports to see images, notes and weather details here",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
fun ReportCard(report: SavedWeatherReportEntity) {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Photo Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Black, shape = MaterialTheme.shapes.medium)
            ) {
                AsyncImage(
                    model = report.snapFile,
                    contentDescription = "Weather Snap",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Weather Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${report.cityName}, ${report.countryName}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = getWeatherCondition(report.weatherCode),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = report.dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "${report.temperature.toInt()}°C",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }

            // Size Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoBox(
                    label = "Original",
                    text = formatFileSize(report.rawSize),
                    textColor = Color(0xFFFFB74D), // Approximated orange
                    modifier = Modifier.weight(1f)
                )

                InfoBox(
                    label = "Compressed",
                    text = formatFileSize(report.compressedSize),
                    textColor = Color(0xFF81C784), // Approximated light green
                    modifier = Modifier.weight(1f)
                )
            }

            // Tag
            report.note?.let {
                ReportNote(note = it)
            }
        }
    }
}

private fun formatFileSize(size: Long): String {
    val kb = size / 1024.0
    return if (kb > 1024) {
        "%.1f MB".format(kb / 1024.0)
    } else {
        "%.0f KB".format(kb)
    }
}

@Composable
fun ReportNote(note: String, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = note,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SavedReportsScreenPreview() {
    WeatherSnapTheme {
        SavedReportsScreen(reports = emptyList(), onExit = {})
    }
}

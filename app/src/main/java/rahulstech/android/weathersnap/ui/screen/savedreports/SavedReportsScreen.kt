package rahulstech.android.weathersnap.ui.screen.savedreports

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rahulstech.android.weathersnap.ui.component.InfoBox
import rahulstech.android.weathersnap.ui.component.WeatherSnapHeader
import rahulstech.android.weathersnap.ui.navigation.AppRoute
import rahulstech.android.weathersnap.ui.theme.WeatherSnapTheme


@Composable
fun SavedReportsRoute(
    onNavigate: (AppRoute) -> Unit,
    onExit: () -> Unit
) {
    SavedReportsScreen(
        onNavigate = onNavigate,
        onExit = onExit
    )
}
@Composable
fun SavedReportsScreen(
    onNavigate: (AppRoute) -> Unit,
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
            subtitle = "1 report stored locally",
            actionLabel = "Back",
            onAction = onExit,
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(count = 5) {
                ReportCard(note = "Test_report")
            }
        }
    }
}

@Composable
fun ReportCard(note: String) {
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
            )

            // Weather Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Ben, Iran",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Partly cloudy",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = "08 May 2026, 12:18 pm",
                        style = MaterialTheme.typography.labelSmall,
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "17°C",
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
                    text = "552 KB",
                    textColor = Color(0xFFFFB74D), // Approximated orange
                    modifier = Modifier.weight(1f)
                )

                InfoBox(
                    label = "Compressed",
                    text = "84 KB",
                    textColor = Color(0xFF81C784), // Approximated light green
                    modifier = Modifier.weight(1f)
                )
            }

            // Tag
            ReportNote(note = note)
        }
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
        SavedReportsScreen(onNavigate = {}, onExit = {})
    }
}

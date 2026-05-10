package rahulstech.android.weathersnap.ui.screen.weatherscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import rahulstech.android.weathersnap.ui.component.WeatherInfoCard
import rahulstech.android.weathersnap.ui.component.WeatherSnapHeader
import rahulstech.android.weathersnap.ui.theme.WeatherSnapTheme

@Composable
fun WeatherScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WeatherSnapHeader(
            title = "WeatherSnap",
            subtitle = "Live weather reports with camera evidence",
            actionLabel = "Reports",
            onAction = {}
        )
        CitySearchBar()
        WeatherInfoSection()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySearchBar() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = "Ben, Iran",
                onValueChange = {},
                label = { Text("City") },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.extraSmall,
                supportingText = {
                    Text("Enter more than 2 letters to start city suggestions.")
                }
            )
            Button(
                onClick = { /* TODO */ },
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Text("Search")
            }
        }

    }
}

@Composable
fun WeatherInfoSection() {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            WeatherInfoCard()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surfaceContainerHigh, shape =MaterialTheme.shapes.small)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Report readiness", color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Camera and Room DB enabled",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Report", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    WeatherSnapTheme {
        WeatherScreen()
    }
}

package rahulstech.android.weathersnap.ui.util

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import rahulstech.android.weathersnap.data.local.entity.SavedWeatherReportEntity
import rahulstech.android.weathersnap.ui.model.ImageCaptureResult
import rahulstech.android.weathersnap.ui.model.WeatherReport
import java.time.LocalDateTime

fun Modifier.shimmer(
    baseColor: Color = Color.LightGray.copy(alpha = 0.3f),
    highlightColor: Color = Color.LightGray.copy(alpha = 0.1f)
): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing)
        ),
        label = "shimmer"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                baseColor,
                highlightColor,
                baseColor,
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}

fun ImageCaptureResult.toEntity(report: WeatherReport, note: String? = null): SavedWeatherReportEntity {
    val city = report.city
    val weather = report.weather
    return SavedWeatherReportEntity(
        cityName = city.name,
        countryName = city.country,
        longitude = city.longitude,
        latitude = city.latitude,
        weatherTime = weather.time,
        temperature = weather.temperature,
        windSpeed = weather.windSpeed,
        weatherCode = weather.weatherCode,
        surfacePressure = weather.surfacePressure,
        humidity = weather.humidity,
        snapFile = this.filePath,
        rawSize = this.rawSize,
        compressedSize = this.compressSize,
        dateTime = LocalDateTime.now(),
        note = note
    )
}

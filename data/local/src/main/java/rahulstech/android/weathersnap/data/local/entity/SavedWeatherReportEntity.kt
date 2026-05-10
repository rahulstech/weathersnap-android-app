package rahulstech.android.weathersnap.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Room entity representing a saved weather report.
 */
@Entity(tableName = "saved_weather_reports")
data class SavedWeatherReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cityName: String,
    val countryName: String,
    val longitude: String,
    val latitude: String,
    // Current weather fields
    val weatherTime: LocalDateTime,
    val temperature: Double,
    val windSpeed: Double,
    val weatherCode: Int,
    val surfacePressure: Double,
    val humidity: Int,
    // File and metadata
    val snapFile: String,
    val rawSize: Long,
    val compressedSize: Long,
    val dateTime: LocalDateTime,
    val note: String? = null
)

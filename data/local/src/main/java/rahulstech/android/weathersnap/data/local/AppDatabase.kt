package rahulstech.android.weathersnap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import rahulstech.android.weathersnap.data.local.converter.RoomConverters
import rahulstech.android.weathersnap.data.local.dao.SavedWeatherReportDao
import rahulstech.android.weathersnap.data.local.entity.SavedWeatherReportEntity

/**
 * The Room database for this app.
 */
@Database(
    entities = [SavedWeatherReportEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedWeatherReportDao(): SavedWeatherReportDao
}

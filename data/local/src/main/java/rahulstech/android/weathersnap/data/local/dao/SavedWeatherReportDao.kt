package rahulstech.android.weathersnap.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import rahulstech.android.weathersnap.data.local.entity.SavedWeatherReportEntity

/**
 * Data Access Object for the saved_weather_reports table.
 */
@Dao
interface SavedWeatherReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(report: SavedWeatherReportEntity): Long

    @Query("SELECT * FROM saved_weather_reports ORDER BY dateTime DESC")
    fun getAll(): Flow<List<SavedWeatherReportEntity>>
}

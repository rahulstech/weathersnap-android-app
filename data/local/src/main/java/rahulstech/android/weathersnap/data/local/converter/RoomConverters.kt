package rahulstech.android.weathersnap.data.local.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Room type converters for custom types.
 */
class RoomConverters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromString(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }

    @TypeConverter
    fun toString(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }
}

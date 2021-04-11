package hr.algebra.triathlist.converters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime

object LocalDateTimeConverter {

    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(localDateTime: LocalDateTime) = localDateTime.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(localDateTimeString: String): LocalDateTime = LocalDateTime.parse(localDateTimeString)
}
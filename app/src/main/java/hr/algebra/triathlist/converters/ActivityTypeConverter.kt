package hr.algebra.triathlist.converters

import androidx.room.TypeConverter
import hr.algebra.triathlist.model.ActivityType

object ActivityTypeConverter {

    @TypeConverter
    @JvmStatic
    fun fromActivityType(activityType: ActivityType) = activityType.ordinal

    @TypeConverter
    @JvmStatic
    fun toActivityType(intValue: Int): ActivityType = enumValues<ActivityType>()[intValue]
}
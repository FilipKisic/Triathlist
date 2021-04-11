package hr.algebra.triathlist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.LocalTime

@Entity(tableName = "activity")
data class Activity(
    var duration: String,
    var calories: Int?,
    var distance: Int,
    var laps: Int?,
    var steps: Int?,
    var dayOfWeek: String,
    var activityStart: LocalDateTime,
    var activityEnd: LocalDateTime,
    var activityType: ActivityType
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var idActivity: Int = 0
}
package hr.algebra.triathlist.model

import androidx.room.Entity

@Entity(tableName = "swim_info")
data class SwimInfo(
    val laps: Int
)
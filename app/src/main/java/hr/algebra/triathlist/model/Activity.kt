package hr.algebra.triathlist.model

import java.time.LocalDateTime

data class Activity(
    val idActivity: Int,
    val duration: Int,
    val calories: Int,
    val activityStart: LocalDateTime,
    val activityEnd: LocalDateTime,
    val activityType: String
)
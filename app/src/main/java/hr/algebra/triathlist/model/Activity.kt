package hr.algebra.triathlist.model

data class Activity(
    val duration: String? = null,
    val calories: Int? = null,
    val distance: Int? = null,
    val laps: Int? = null,
    val steps: Int? = null,
    val dayOfWeek: String? = null,
    val activityStart: String? = null,
    val activityEnd: String? = null,
    val activityType: Int? = null,
    val userEmail: String? = null
)
// Null default values create a no-argument default constructor, which is needed
// for deserialization from a DataSnapshot.
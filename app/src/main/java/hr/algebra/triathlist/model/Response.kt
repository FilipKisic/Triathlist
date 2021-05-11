package hr.algebra.triathlist.model

import java.lang.Exception

data class Response(var activities: List<Activity>? = null, var exception: Exception? = null)
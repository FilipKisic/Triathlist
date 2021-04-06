package hr.algebra.triathlist.services

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import hr.algebra.triathlist.api.WeatherFetcher

private const val JOB_ID = 1

class WeatherService : JobIntentService() {
    override fun onHandleWork(intent: Intent) {
        WeatherFetcher(this).fetchData(lat, long)
    }

    companion object {
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, WeatherService::class.java, JOB_ID, intent)
        }
    }

}
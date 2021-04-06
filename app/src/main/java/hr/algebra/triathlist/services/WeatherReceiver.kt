package hr.algebra.triathlist.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.triathlist.MainActivity
import hr.algebra.triathlist.framework.startActivity

class WeatherReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        context.startActivity<MainActivity>()
    }
}
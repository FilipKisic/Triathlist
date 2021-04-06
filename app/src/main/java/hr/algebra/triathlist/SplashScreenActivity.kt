package hr.algebra.triathlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import hr.algebra.triathlist.framework.isOnline
import hr.algebra.triathlist.services.WeatherService
import hr.algebra.triathlist.services.getCurrentLocation
import pub.devrel.easypermissions.EasyPermissions

lateinit var fusedLocationClient: FusedLocationProviderClient

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (getCurrentLocation())
            loadData()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        loadData()
    }

    private fun loadData() {
        if (isOnline()) {
            Intent(this, WeatherService::class.java).apply {
                WeatherService.enqueueWork(this@SplashScreenActivity, this)
            }
        } else {
            Toast.makeText(this, getString(R.string.no_connection_available), Toast.LENGTH_LONG).show()
            finish()
        }
    }
}

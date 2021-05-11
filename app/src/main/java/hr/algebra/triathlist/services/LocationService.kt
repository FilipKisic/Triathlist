package hr.algebra.triathlist.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import hr.algebra.triathlist.R
import hr.algebra.triathlist.fusedLocationClient
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception

private lateinit var locationRequest: LocationRequest
private lateinit var locationCallback: LocationCallback
private const val REQUEST_CHECK_SETTINGS = 65
private const val LOCATION_REQUEST_CODE = 124
private const val INTERVAL = 3000L
private const val FASTEST_INTERVAL = 2000L
var lat = 0.0
var long = 0.0
var locationUpdateState = false

@SuppressLint("MissingPermission")
@AfterPermissionGranted(LOCATION_REQUEST_CODE)
fun Activity.getCurrentLocation(): Boolean {
    if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lat = location.latitude
                long = location.longitude
            }
        }
        return true
    } else {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.permission_rationale),
            LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return false
    }
}

fun Activity.getLocationUpdates(function: () -> (Unit), fusedLocationProviderClient: FusedLocationProviderClient) {
    if (hasLocationPermission(this)) {
        createLocationCallback(function)
        createLocationRequest(fusedLocationProviderClient)
    } else requestPermission()
}

private fun hasLocationPermission(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

fun Activity.requestPermission() {
    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
}

fun Activity.createLocationRequest(fusedLocationProviderClient: FusedLocationProviderClient) {
    locationRequest = LocationRequest.create().apply {
        interval = INTERVAL
        fastestInterval = FASTEST_INTERVAL
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val client = LocationServices.getSettingsClient(this)
    val task = client.checkLocationSettings(builder.build())
    task.addOnSuccessListener {
        locationUpdateState = true
        startLocationUpdates(fusedLocationProviderClient)
    }
    task.addOnFailureListener { e ->
        if (e is ResolvableApiException) {
            try {
                e.startResolutionForResult(this, REQUEST_CHECK_SETTINGS) //show dialog
            } catch (ignored: Exception) {

            }
        }
    }
}

fun createLocationCallback(function: () -> Unit) {
    locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            if (locationResult.locations.isNotEmpty()) {
                //currentLocation = locationResult.lastLocation
                function()
            }
        }
    }
}

@SuppressLint("MissingPermission")
fun Activity.startLocationUpdates(fusedLocationProviderClient: FusedLocationProviderClient) {
    if (!hasLocationPermission(this))
        requestPermission()
    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
}

fun stopLocationUpdates(fusedLocationProviderClient: FusedLocationProviderClient) {
    fusedLocationProviderClient.removeLocationUpdates(locationCallback)
}
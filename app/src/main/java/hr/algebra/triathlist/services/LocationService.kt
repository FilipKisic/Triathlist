package hr.algebra.triathlist.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import hr.algebra.triathlist.R
import hr.algebra.triathlist.fusedLocationClient
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

private const val LOCATION_REQUEST_CODE = 124
var lat = 0.0
var long = 0.0

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
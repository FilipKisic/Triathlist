package hr.algebra.triathlist.framework

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import hr.algebra.triathlist.R
import hr.algebra.triathlist.components.InfoTextView
import hr.algebra.triathlist.components.SessionGoalCard
import hr.algebra.triathlist.fusedLocationClient
import hr.algebra.triathlist.services.lat
import hr.algebra.triathlist.services.long
import kotlinx.android.synthetic.main.info_text_view.view.*
import kotlinx.android.synthetic.main.session_goal_card.view.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

const val CLASS = "Class"
const val ACTIVITY_REQUEST_CODE = 65

inline fun <reified T : Activity> Context.startActivity() = Intent(this, T::class.java).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(this)
}

inline fun <reified T : Activity> Context.startActivity(className: String) = Intent(this, T::class.java).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    putExtra(CLASS, className)
    startActivity(this)
}

inline fun <reified T : Activity> Context.startPreviousActivity() = Intent(this, T::class.java).apply {
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(this)
}

inline fun <reified T : Activity> Fragment.startActivity() = startActivity(Intent(requireContext(), T::class.java))
inline fun <reified T : BroadcastReceiver> Context.sendBroadcast() = sendBroadcast(Intent(this, T::class.java))

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    if (network != null) {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        if (networkCapabilities != null) {
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
    return false
}

fun SessionGoalCard.isValid(context: Context, unit: String): Boolean {
    if (this.etInput.text.toString().toInt() <= 0) {
        Toast.makeText(context, "$unit value must be greater than 0", Toast.LENGTH_LONG).show()
        return false
    }
    return true
}

fun EditText.isValid(context: Context, property: String): Boolean {
    if (this.text.toString().isBlank()) {
        Toast.makeText(context, "$property value must not be empty", Toast.LENGTH_LONG).show()
        return false
    }
    return true
}

fun EditText.isNumberValid(context: Context, property: String): Boolean {
    if (this.text.toString().toIntOrNull() == null) {
        Toast.makeText(context, "$property value must be a number", Toast.LENGTH_LONG).show()
        return false
    }
    return true
}
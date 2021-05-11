package hr.algebra.triathlist

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import hr.algebra.triathlist.framework.startPreviousActivity
import hr.algebra.triathlist.model.Activity
import hr.algebra.triathlist.model.ActivityType
import hr.algebra.triathlist.services.*
import kotlinx.android.synthetic.main.action_button.view.*
import kotlinx.android.synthetic.main.activity_cycling.*
import timerx.Stopwatch
import timerx.StopwatchBuilder
import timerx.Timer
import timerx.TimerBuilder
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

private lateinit var buttonState: String
private lateinit var stopwatch: Stopwatch
private lateinit var timer: Timer
private lateinit var timeOfStart: LocalDateTime
private lateinit var database: FirebaseDatabase
private lateinit var reference: DatabaseReference
private lateinit var auth: FirebaseAuth
private lateinit var previousLocation: Location

class CyclingActivity : AppCompatActivity() {

    private var goalDistance = 0
    private var currentDistance = 0f
    private var togo = 0
    private var pause = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cycling)

        initData()
        initListeners()
        setupLocation()
    }

    override fun onResume() {
        super.onResume()
        if (!locationUpdateState) startLocationUpdates(fusedLocationClient)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates(fusedLocationClient)
    }

    private fun setupLocation() {
        createLocationCallback(::calculateDistance)
        createLocationRequest(fusedLocationClient)
    }

    @SuppressLint("MissingPermission")
    private fun calculateDistance() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null && ::previousLocation.isInitialized && location != previousLocation) {
                currentDistance += previousLocation.distanceTo(location)
                previousLocation = location
                updateUI()
            } else if (location != null)
                previousLocation = location
        }
    }

    private fun updateUI() {
        tvCyclingTotalDistance.text = "${currentDistance.toInt()}m"
        tvCyclingToGo.text = "${(goalDistance - currentDistance).toInt()}m"
        pb_cycling_goal.progress = ((currentDistance / goalDistance) * 100).toInt()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initData() {
        getData()
        showData()
        initStopwatchAndTimer()
        pb_cycling_goal.progress = 0
        buttonState = btnStartStopCycling.buttonText
        timeOfStart = LocalDateTime.now()
        auth = FirebaseAuth.getInstance()
    }

    private fun getData() {
        goalDistance = intent.getIntExtra("goalDistance", 0) * 1000
        pause = intent.getIntExtra("pause", 0)
    }

    private fun showData() {
        tvCyclingTotalDistance.text = currentDistance.toString()
        tvCyclingToGo.text = goalDistance.toString()
        tvCyclingPause.text = if (pause < 10) "0$pause:00" else "$pause:00"
    }

    private fun initStopwatchAndTimer() {
        initStopwatch()
        initTimer()
        timer.start()
        timer.stop()
    }

    private fun initStopwatch() {
        stopwatch = StopwatchBuilder().apply {
            startFormat("HH:MM:SS")
            onTick { time -> tvCyclingStopwatch.text = time }
        }.build()
    }

    private fun initTimer() {
        timer = TimerBuilder().apply {
            startTime(pause.toLong(), TimeUnit.MINUTES)
            startFormat("MM:SS")
            onTick { time -> tvCyclingPause.text = time }
            onFinish { resumeSession() }
        }.build()
    }

    private fun resumeSession() {
        buttonState = "Stop"
        btnStartStopCycling.tvButtonText.text = buttonState
        stopwatch.start()
        timer.stop()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        btnStartStopCycling.setOnClickListener {
            toggleState()
            buttonState = btnStartStopCycling.tvButtonText.text.toString()
            if (buttonState == "Stop") resumeSession() else pauseSession()
        }
        btnFinishCycling.setOnClickListener {
            finishSession()
        }
    }

    private fun toggleState() = btnStartStopCycling.tvButtonText.setText(if (buttonState == "Start") R.string.stop else R.string.start)

    private fun pauseSession() {
        stopwatch.stop()
        timer.start()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun finishSession() {
        pb_cycling_goal.progress = 100
        stopwatch.stop()
        btnStartStopCycling.tvButtonText.setText(R.string.start)
        saveToFirebase()
        resetValues()
        startPreviousActivity<MainActivity>()
    }

    private fun resetValues() {
        goalDistance = 0
        currentDistance = 0f
        togo = 0
        pause = 0
        pb_cycling_goal.progress = 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveToFirebase() {
        val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val activity = Activity(
            tvCyclingStopwatch.text.toString(),
            0,
            currentDistance.toInt(),
            0,
            0,
            dayOfWeekFormat.format(Date()).toString(),
            timeOfStart.toString(),
            LocalDateTime.now().toString(),
            ActivityType.CYCLING.ordinal,
            auth.currentUser!!.email
        )
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Activities")
        val id = reference.push().key
        reference.child(id!!).setValue(activity)
    }
}
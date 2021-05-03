package hr.algebra.triathlist

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.triathlist.database.TriathlistDatabase
import hr.algebra.triathlist.framework.startPreviousActivity
import hr.algebra.triathlist.model.Activity
import hr.algebra.triathlist.model.ActivityType
import kotlinx.android.synthetic.main.action_button.view.*
import kotlinx.android.synthetic.main.activity_running.*
import kotlinx.android.synthetic.main.activity_session.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
private lateinit var sensorManager: SensorManager

class RunningActivity : AppCompatActivity(), SensorEventListener {

    private var goalDistance = 0
    private var currentDistance = 0
    private var steps = 0
    private var togo = 0
    private var repetitions = 0
    private var pause = 0
    private var isRunning: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        initData()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) Toast.makeText(this, "No Sensor", Toast.LENGTH_LONG).show()
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        isRunning = false
        sensorManager.unregisterListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        resetValues()
        stopwatch.stop()
        timer.stop()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initData() {
        getData()
        showData()
        initStopwatchAndTimer()
        pb_run_goal.progress = 0
        buttonState = btnStartStopRunning.buttonText
        timeOfStart = LocalDateTime.now()
    }

    private fun getData() {
        togo = intent.getIntExtra("repDistance", 0)
        repetitions = intent.getIntExtra("repetitions", 0)
        goalDistance = togo * repetitions
        pause = intent.getIntExtra("pause", 0)
    }

    private fun showData() {
        tvRunningTotalLength.text = currentDistance.toString()
        tvRunningSteps.text = steps.toString()
        tvRunningToGo.text = togo.toString()
        tvRunningPause.text = if (pause < 10) "0$pause:00" else "$pause:00"
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
            onTick { time -> tvRunStopwatch.text = time }
        }.build()
    }

    private fun initTimer() {
        timer = TimerBuilder().apply {
            startTime(pause.toLong(), TimeUnit.MINUTES)
            startFormat("MM:SS")
            onTick { time -> tvRunningPause.text = time }
            onFinish { resumeSession() }
        }.build()
    }

    private fun resumeSession() {
        buttonState = "Stop"
        btnStartStopRunning.tvButtonText.text = buttonState
        stopwatch.start()
        timer.stop()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        btnStartStopRunning.setOnClickListener {
            toggleState()
            buttonState = btnStartStopRunning.tvButtonText.text.toString()
            if (buttonState == "Stop") resumeSession() else pauseSession()
        }
        btnFinishRunning.setOnClickListener {
            finishSession()
        }
    }

    private fun toggleState() = btnStartStopRunning.tvButtonText.setText(if (buttonState == "Start") R.string.stop else R.string.start)

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (isRunning) {
            tvRunningSteps.text = "${event.values[0]}"
            steps = event.values[0].toInt()
            currentDistance = ((event.values[0] * 75) / 100_000).toInt()
        }
    }

    private fun pauseSession() {
        stopwatch.stop()
        timer.start()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun finishSession() {
        pb_run_goal.progress = 100
        stopwatch.stop()
        btnStartStopRunning.tvButtonText.setText(R.string.start)
        saveInDatabase()
        resetValues()
        startPreviousActivity<MainActivity>()
    }

    private fun resetValues() {
        goalDistance = 0
        currentDistance = 0
        steps = 0
        togo = 0
        repetitions = 0
        pause = 0
        pb_run_goal.progress = 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveInDatabase() {
        val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val db = TriathlistDatabase.getDatabase(this)
        val activity = Activity(
            tvRunStopwatch.text.toString(),
            null,
            currentDistance,
            null,
            steps,
            dayOfWeekFormat.format(Date()).toString(),
            timeOfStart,
            LocalDateTime.now(),
            ActivityType.RUNNING
        )
        GlobalScope.launch {
            db.activityDao().insert(activity)
        }
    }
}

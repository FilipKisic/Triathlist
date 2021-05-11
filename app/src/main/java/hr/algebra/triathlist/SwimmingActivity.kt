package hr.algebra.triathlist

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.github.nisrulz.sensey.Sensey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import hr.algebra.triathlist.framework.startPreviousActivity
import hr.algebra.triathlist.model.Activity
import hr.algebra.triathlist.model.ActivityType
import kotlinx.android.synthetic.main.action_button.view.*
import kotlinx.android.synthetic.main.activity_session.*
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

class SwimmingActivity : AppCompatActivity() {

    private var goalDistance = 0
    private var currentDistance = 0
    private var laps = 0
    private var togo = 0
    private var repetitions = 0
    private var pause = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)
        Sensey.getInstance().init(this)
        initData()
        initListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        resetValues()
        stopwatch.stop()
        timer.stop()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initData() {
        refreshData()
        showData()
        initStopwatchAndTimer()
        pb_swim_goal.progress = 0
        buttonState = btnStartStop.buttonText
        timeOfStart = LocalDateTime.now()
        auth = FirebaseAuth.getInstance()
    }

    private fun refreshData() {
        togo = intent.getIntExtra("repDistance", 0)
        repetitions = intent.getIntExtra("repetitions", 0)
        goalDistance = togo * repetitions
        pause = intent.getIntExtra("pause", 0)
    }

    private fun showData() {
        tvTotalLength.text = currentDistance.toString()
        tvLaps.text = laps.toString()
        tvToGo.text = togo.toString()
        tvPause.text = if (pause < 10) "0$pause:00" else "$pause:00"
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
            onTick { time -> tvStopwatch.text = time }
        }.build()
    }

    private fun initTimer() {
        timer = TimerBuilder().apply {
            startTime(pause.toLong(), TimeUnit.MINUTES)
            startFormat("MM:SS")
            onTick { time -> tvPause.text = time }
            onFinish { resumeSession() }
        }.build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        btnStartStop.setOnClickListener {
            toggleState()
            buttonState = btnStartStop.tvButtonText.text.toString()
            if (buttonState == "Stop") resumeSession() else pauseSession()
        }
        btnFinish.setOnClickListener {
            finishSession()
        }
        Sensey.getInstance().startWaveDetection {
            if (buttonState == "Stop") {
                currentDistance += 50
                laps++
                togo -= 50
                checkState()
                showData()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkState() {
        if (currentDistance != goalDistance) {
            if (togo == 0) {
                toggleState()
                pauseSession()
                refreshData()
            }
            pb_swim_goal.progress = ((currentDistance.toFloat() / goalDistance.toFloat()) * 100).toInt()
        } else finishSession()
    }

    private fun pauseSession() {
        stopwatch.stop()
        timer.start()
    }

    private fun resumeSession() {
        buttonState = "Stop"
        btnStartStop.tvButtonText.text = buttonState
        stopwatch.start()
        timer.stop()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun finishSession() {
        pb_swim_goal.progress = 100
        stopwatch.stop()
        btnStartStop.tvButtonText.setText(R.string.start)
        saveToFirebase()
        resetValues()
        startPreviousActivity<MainActivity>()
    }

    private fun resetValues() {
        goalDistance = 0
        currentDistance = 0
        laps = 0
        togo = 0
        repetitions = 0
        pause = 0
        pb_swim_goal.progress = 0
    }

    private fun toggleState() = btnStartStop.tvButtonText.setText(if (buttonState == "Start") R.string.stop else R.string.start)

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveToFirebase() {
        val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val activity = Activity(
            tvStopwatch.text.toString(),
            0,
            currentDistance,
            laps,
            0,
            dayOfWeekFormat.format(Date()).toString(),
            timeOfStart.toString(),
            LocalDateTime.now().toString(),
            ActivityType.SWIMMING.ordinal,
            auth.currentUser!!.email
        )
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Activities")
        val id = reference.push().key
        reference.child(id!!).setValue(activity)
    }
}
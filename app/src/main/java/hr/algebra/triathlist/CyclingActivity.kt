package hr.algebra.triathlist

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.triathlist.framework.startPreviousActivity
import kotlinx.android.synthetic.main.action_button.view.*
import kotlinx.android.synthetic.main.activity_cycling.*
import kotlinx.android.synthetic.main.activity_running.*
import timerx.Stopwatch
import timerx.StopwatchBuilder
import timerx.Timer
import timerx.TimerBuilder
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

private lateinit var buttonState: String
private lateinit var stopwatch: Stopwatch
private lateinit var timer: Timer
private lateinit var timeOfStart: LocalDateTime

class CyclingActivity : AppCompatActivity() {

    private var goalDistance = 0
    private var currentDistance = 0
    private var togo = 0
    private var pause = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cycling)

        initData()
        initListeners()
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
        goalDistance = intent.getIntExtra("goalDistance", 0)
        pause = intent.getIntExtra("pause", 0)
    }

    private fun showData() {
        tvCyclingTotalDistance.text = currentDistance.toString()
        tvCyclingToGo.text = togo.toString()
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

    private fun initListeners() {
        btnStartStopCycling.setOnClickListener {
            toggleState()
            buttonState = btnStartStopRunning.tvButtonText.text.toString()
            if (buttonState == "Stop") resumeSession() else pauseSession()
        }
        btnFinishRunning.setOnClickListener {
            finishSession()
        }
    }

    private fun toggleState() = btnStartStopRunning.tvButtonText.setText(if (buttonState == "Start") R.string.stop else R.string.start)

    private fun pauseSession() {
        stopwatch.stop()
        timer.start()
    }

    private fun finishSession() {
        pb_run_goal.progress = 100
        stopwatch.stop()
        btnStartStopRunning.tvButtonText.setText(R.string.start)
        //saveInDatabase()
        resetValues()
        startPreviousActivity<MainActivity>()
    }

    private fun resetValues() {
        goalDistance = 0
        currentDistance = 0
        togo = 0
        pause = 0
        pb_cycling_goal.progress = 0
    }
}
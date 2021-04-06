package hr.algebra.triathlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.triathlist.framework.CLASS
import hr.algebra.triathlist.framework.isValid
import kotlinx.android.synthetic.main.activity_new_session.*
import kotlinx.android.synthetic.main.session_goal_card.view.*

class NewSessionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_session)
        initListeners()
    }

    private fun initListeners() {
        val classType = intent.getStringExtra(CLASS)
        btnStart.setOnClickListener {
            if (isValid()) {
                if (classType.equals(SWIM_ACTIVITY))
                    startSession<SwimmingActivity>()
                else startSession<RunningActivity>()
            }
        }
        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun isValid(): Boolean {
        var isOkay = true
        if (!sgcGoalRepMeters.isValid(this, "Meters")) isOkay = false
        if (!sgcGoalRepetition.isValid(this, "Repetition")) isOkay = false
        if (!sgcGoalPause.isValid(this, "Minutes")) isOkay = false
        return isOkay
    }

    private inline fun <reified T : Activity> startSession() {
        val intent = Intent(this, T::class.java).apply {
            putExtra("repDistance", sgcGoalRepMeters.etInput.text.toString().toInt())
            putExtra("repetitions", sgcGoalRepetition.etInput.text.toString().toInt())
            putExtra("pause", sgcGoalPause.etInput.text.toString().toInt())
        }
        startActivity(intent)
    }
}
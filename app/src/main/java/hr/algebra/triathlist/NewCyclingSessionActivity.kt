package hr.algebra.triathlist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_new_cycling_session.*
import kotlinx.android.synthetic.main.activity_new_session.*
import kotlinx.android.synthetic.main.session_goal_card.view.*

class NewCyclingSessionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_cycling_session)
        initListeners()
    }

    private fun initListeners() {
        btnCyclingStart.setOnClickListener {
            startSession()
        }

        btnCyclingCancel.setOnClickListener {
            finish()
        }
    }

    private fun startSession() {
        val intent = Intent(this, CyclingActivity::class.java).apply {
            putExtra("goalDistance", sgcGoalRepKilometers.etInput.text.toString().toInt())
            putExtra("pause", sgcGoalCyclingPause.etInput.text.toString().toInt())
        }
        startActivity(intent)
    }
}
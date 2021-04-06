package hr.algebra.triathlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.triathlist.framework.startActivity
import kotlinx.android.synthetic.main.activity_menu.*

const val SWIM_ACTIVITY = "Swim"
const val RUN_ACTIVITY = "Run"

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        initListeners()
    }

    private fun initListeners() {
        scSwimming.setOnClickListener {
            startActivity<NewSessionActivity>(SWIM_ACTIVITY)
        }
        scRunning.setOnClickListener {
            startActivity<NewSessionActivity>(RUN_ACTIVITY)
        }
        scCycling.setOnClickListener {
            startActivity<NewCyclingSessionActivity>()
        }
    }
}
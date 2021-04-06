package hr.algebra.triathlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.triathlist.framework.startActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initListeners()
    }

    private fun initListeners() {
        btnRegisterActivity.setOnClickListener {
            startActivity<SplashScreenActivity>()
        }
    }
}
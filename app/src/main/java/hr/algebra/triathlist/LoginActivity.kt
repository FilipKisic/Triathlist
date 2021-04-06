package hr.algebra.triathlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.triathlist.framework.startActivity
import kotlinx.android.synthetic.main.activity_login_page.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        initListeners()
    }

    private fun initListeners() {
        btnLoginActivity.setOnClickListener {
            startActivity<SplashScreenActivity>()
        }
    }
}
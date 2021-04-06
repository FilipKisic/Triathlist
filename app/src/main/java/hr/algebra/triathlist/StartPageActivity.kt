package hr.algebra.triathlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.triathlist.framework.startActivity
import kotlinx.android.synthetic.main.activity_start_page.*

class StartPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_page)
        initListeners()
    }

    private fun initListeners() {
        btnLogin.setOnClickListener {
            startActivity<LoginActivity>()
        }
        btnRegister.setOnClickListener {
            startActivity<RegisterActivity>()
        }
    }
}
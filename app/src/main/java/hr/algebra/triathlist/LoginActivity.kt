package hr.algebra.triathlist

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.triathlist.framework.isValid
import hr.algebra.triathlist.framework.startActivity
import kotlinx.android.synthetic.main.activity_login_page.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        auth = FirebaseAuth.getInstance()
        initListeners()
    }

    private fun initListeners() {
        btnLoginActivity.setOnClickListener {
            if (areFieldsValid()) {
                val email = etLoginEmail.text.toString()
                val password = etLoginPassword.text.toString()
                signInUser(email, password)
            }
        }
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                    startActivity<SplashScreenActivity>()
                else Toast.makeText(this, "User does not exist", Toast.LENGTH_LONG).show()
            }
    }

    private fun areFieldsValid(): Boolean {
        var isValid = true
        if (!etLoginEmail.isValid(this, "Email")) isValid = false
        if (!etLoginPassword.isValid(this, "Password")) isValid = false
        return isValid
    }
}
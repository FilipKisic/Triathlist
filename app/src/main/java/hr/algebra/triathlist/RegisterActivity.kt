package hr.algebra.triathlist

import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import hr.algebra.triathlist.framework.isNumberValid
import hr.algebra.triathlist.framework.isValid
import hr.algebra.triathlist.framework.startActivity
import hr.algebra.triathlist.model.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.info_text_view.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        initListeners()
    }

    private fun initListeners() {
        btnRegisterActivity.setOnClickListener {
            if (areFieldsValid()) {
                val email = etRegisterEmail.text.toString()
                val password = etRegisterPassword.text.toString()
                createUser(email, password)
            }
        }
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                    startActivity<SplashScreenActivity>()
                else Toast.makeText(baseContext, "Registration failed", Toast.LENGTH_LONG).show()
            }
    }

    private fun areFieldsValid(): Boolean {
        var isValid = true
        if (!etRegisterEmail.isValid(this, "Email")) isValid = false
        if (!etRegisterPassword.isValid(this, "Password") && etRegisterPassword.text.length >= 6) isValid = false
        return isValid
    }
}
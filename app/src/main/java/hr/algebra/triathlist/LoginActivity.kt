package hr.algebra.triathlist

import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.triathlist.database.TriathlistDatabase
import hr.algebra.triathlist.framework.isValid
import hr.algebra.triathlist.framework.startActivity
import hr.algebra.triathlist.model.User
import kotlinx.android.synthetic.main.activity_login_page.*
import kotlinx.android.synthetic.main.info_text_view.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginActivity : AppCompatActivity() {
    private var loggedUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        initListeners()
        itvLoginEmail.etInfo.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        itvLoginPassword.etInfo.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    private fun initListeners() {
        btnLoginActivity.setOnClickListener {
            if (areFieldsValid() && isUserAuthenticated()) {
                startActivity<SplashScreenActivity>()
            } else Toast.makeText(this, "User does not exist", Toast.LENGTH_LONG).show()
        }
    }

    private fun areFieldsValid(): Boolean {
        var isValid = true
        if (!itvLoginEmail.isValid(this, "Email")) isValid = false
        if (!itvLoginPassword.isValid(this, "Password")) isValid = false
        return isValid
    }

    //ASK
    private fun isUserAuthenticated(): Boolean = runBlocking {
        val db = TriathlistDatabase.getDatabase(applicationContext)
        val job = GlobalScope.launch {
            loggedUser = db.userDao().authenticateUser(itvLoginEmail.etInfo.text.toString(), itvLoginPassword.etInfo.text.toString())
        }
        job.join()
        return@runBlocking loggedUser != null
    }
}
package hr.algebra.triathlist

import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import hr.algebra.triathlist.database.TriathlistDatabase
import hr.algebra.triathlist.framework.isNumberValid
import hr.algebra.triathlist.framework.isValid
import hr.algebra.triathlist.framework.startActivity
import hr.algebra.triathlist.model.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.info_text_view.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initListeners()
        setInputTypes()
    }

    private fun initListeners() {
        btnRegisterActivity.setOnClickListener {
            if (areFieldsValid()) {
                registerUser()
                startActivity<SplashScreenActivity>()
            }
        }
    }

    private fun setInputTypes(){
        itvRegisterEmail.etInfo.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        itvRegisterPassword.etInfo.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        itvRegisterHeight.etInfo.inputType = InputType.TYPE_CLASS_NUMBER
        itvRegisterWeight.etInfo.inputType = InputType.TYPE_CLASS_NUMBER
        itvRegisterAge.etInfo.inputType = InputType.TYPE_CLASS_NUMBER
    }

    private fun areFieldsValid(): Boolean {
        var isValid = true
        if (!itvRegisterEmail.isValid(this, "Email")) isValid = false
        if (!itvRegisterPassword.isValid(this, "Password")) isValid = false
        if (!itvRegisterHeight.isNumberValid(this, "Height")) isValid = false
        if (!itvRegisterWeight.isNumberValid(this, "Weight")) isValid = false
        if (!itvRegisterAge.isNumberValid(this, "Age")) isValid = false
        return isValid
    }

    private fun registerUser() {
        val user = User(
            itvRegisterEmail.etInfo.text.toString(),
            itvRegisterPassword.etInfo.text.toString(),
            null,
            null,
            null,
            itvRegisterAge.etInfo.text.toString().toInt(),
            itvRegisterHeight.etInfo.text.toString().toInt(),
            itvRegisterWeight.etInfo.text.toString().toInt()
        )
        val db = TriathlistDatabase.getDatabase(this)
        GlobalScope.launch {
            db.userDao().insertUser(user)
        }
    }
}
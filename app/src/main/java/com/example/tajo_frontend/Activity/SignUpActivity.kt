package com.example.tajo_frontend.Activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import com.example.tajo_frontend.API.AccountAPI
import com.example.tajo_frontend.R
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_signup)
    }

    fun OnBtnClick(view: View) {
        when (view.id) {
            R.id.btnSignUp -> {
                val id = etSignUpId.text.toString()
                val pw = etSignUpPw.text.toString()
                val api = AccountAPI()
                api.signUpBus(id, pw, this)
            }
        }
    }
    fun showToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
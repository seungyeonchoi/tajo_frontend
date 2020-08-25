package com.example.tajo_frontend.Activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import com.example.tajo_frontend.API.AccountAPI
import com.example.tajo_frontend.R
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_signup)
    }
    fun OnBtnClick(view: View) {
        when (view.id) {
            R.id.btnSignUp -> {
                val id = etSignUpId.text.toString()
                val pw = etSignUpPw.text.toString()
                val api = AccountAPI()
                api.signUpBus(id,pw,this)
//                if (signUptypeGroup.checkedRadioButtonId == R.id.btnSignUpUser){
//                    val name = etName.text.toString()
//                    api.signUpUser(id ,pw,name,this)
//                }
//                else{
//                    api.signUpBus(id,pw,this)
//                }
            }
//            R.id.btnSignUpUser -> {
//                etId2.hint = "아이디를 입력하세요"
//                etPw2.hint = "비밀번호를 입력하세요"
//                etName.hint = "닉네임을 입력하세요"
//                etName.isEnabled = true
//            }
//            R.id.btnSignUpBus ->{
//                etId2.hint = "차량 번호를 입력하세요"
//                etPw2.hint = "노선번호를 입력하세요"
//                etName.hint = ""
//                etName.isEnabled = false
//
//            }
        }
    }
    fun showToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
package com.example.tajo_frontend.Activity

import NetworkManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.tajo_frontend.API.AccountAPI
import com.example.tajo_frontend.Data.Bus
import com.example.tajo_frontend.R

import kotlinx.android.synthetic.main.activity_login.*


class SignInActivity : AppCompatActivity() {
    val networkManager = NetworkManager()
    private lateinit var bus: Bus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    //    networkManager.createSocket()
      //  readNetworkData()

    }

    override fun onStart() {
        super.onStart()
        Log.i("info","onstart")
    }
    fun OnBtnClick(view: View) {
        when (view.id) {
            R.id.btnLogin -> {
//                val api2 = BuzzerServiceAPI()
//                api2.getBookInfo(this)
                //try1
                val id = etId.text.toString()
                val api = AccountAPI()
                api.signInBus(id,this)
//                if (typeGroup.checkedRadioButtonId == R.id.btnUser){
//                    val pw = etPw.text.toString()
//                    api.signInUser(id ,pw,this)
//                }
//                else{
//                    api.signInBus(id,this)
//                }
                //trt2
//                val data = JSONObject()
////                data.put("type","login")
////                data.put("id",id)
//                networkManager.sendData(data.toString())
            }
            R.id.btnSignUpStart -> {
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
            }
//            R.id.btnUser -> {
//                ivIcon.setImageResource(R.drawable.ic_driver)
//                etId.hint = "아이디를 입력하세요"
//                etPw.isEnabled = true
//            }
//            R.id.btnBus ->{
//                ivIcon.setImageResource(R.drawable.ic_bus)
//                etId.hint = "차량 번호를 입력하세요(ex. 52가3108)"
//                etPw.isEnabled = false
//            }
        }
    }
 //   fun readNetworkData(){
//        networkManager.dataSubject
//            .subscribeOn(AndroidSchedulers.mainThread())
//            .observeOn(AndroidSchedulers.mainThread())
//            .map {
//                Log.i("info","loginactivity: "+it)
//                JSONObject(it).getBoolean("result")
//            }
//            .subscribe{
//                when(it) {
//                    true -> {
//                        val intent = Intent(this, BellActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
//                    false -> showToast("등록되지 않은 차량번호입니다.")
//                }
//            }
//    }
    fun setBus(id:String, bus: Bus){
        this.bus = bus
        this.bus.bus_id = id
    }

    fun changeActivity(){
        val intent = Intent(this, BellActivity::class.java)
        intent.putExtra("bus",bus); /*송신*/
        startActivity(intent)
        etId.setText("")
       // this.finish()
    }

    fun showToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}
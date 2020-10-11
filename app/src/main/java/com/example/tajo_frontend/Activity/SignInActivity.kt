package com.example.tajo_frontend.Activity

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
    private lateinit var bus: Bus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //  networkManager.createSocket()
        //  readNetworkData()
    }

    override fun onStart() {
        super.onStart()
        Log.i("info","onstart")
    }
    fun OnBtnClick(view: View) {
        when (view.id) {
            //예시 id: 12가3456
            R.id.btnLogin -> {
                val id = etId.text.toString()
                val api = AccountAPI()
                api.signInBus(id,this)
            }
            R.id.btnSignUpStart -> {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }
    fun setBus(id:String, bus: Bus){
        this.bus = bus
        this.bus.bus_id = id
    }

    fun changeActivity(){
        val intent = Intent(this, BellActivity::class.java)
        intent.putExtra("bus",bus); /*송신*/
        startActivity(intent)
        etId.setText("")
    }

    fun showToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}
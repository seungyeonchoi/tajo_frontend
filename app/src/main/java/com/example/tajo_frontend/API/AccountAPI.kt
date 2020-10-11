package com.example.tajo_frontend.API

import android.app.Activity
import android.util.Log
import com.example.tajo_frontend.Data.Bus
import com.example.tajo_frontend.Activity.SignInActivity
import com.example.tajo_frontend.Activity.SignUpActivity
import com.example.tajo_frontend.address
import com.example.tajo_frontend.port

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

class AccountAPI {
    //private val URL = "http://10.0.2.2:1337/"
    private val URL = address+":"+ port+"/"
    data class Accepted(
        val message:String
    )
    data class UserInfo(
        val user_id:String,
        val token:String
    )
    /**
     * SignIn API를 통한 사용자 로그인
     *
     * @param uid - 가입할 user id
     * @param upw - 가입할 user pw
     *
     */

    fun signInUser(id: String, pw: String,  activity: SignInActivity) {
        //Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl(this.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //retrofit 객체를 통해 인터페이스 생성
        val service = retrofit.create(SignInService::class.java)

        //Body에 담을 데이터 생성
      //  val body = UserInfo3(user_id = id, user_password = pw)
        val body = HashMap<String, String>()
        body.put("user_id", id)
        body.put("user_password",pw)

        service.signInUser(body)?.enqueue(object : Callback<UserInfo> {
            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                Log.d(
                    "SignInAPI::", "Failed API call with call: " + call +
                            " + exception: " + t
                )
                activity.showToast("로그인에 실패했습니다! 다시 시도해주세요!")
            }
            override fun onResponse(call: Call<UserInfo>, response: retrofit2.Response<UserInfo>) {
                Log.d("Response:: ", response.toString())
                Log.d("Response:: ", response.body().toString())
               // val a = JSONObject(response.errorBody().toString())

                if (response.code() == 200){
                    activity.showToast("로그인에 성공하였습니다.")
                    activity.setResult(Activity.RESULT_OK)
                    activity.finish()
                }
                else if (response.code() == 401){ //400 -> message 내용에 따른 error handeling 필요함
                    activity.showToast("회원 정보가 일치하지 않습니다.")
                }
                else{
                    activity.showToast("로그인에 실패하였습니다.")
                }
            }
        })
//        service.getBookInfo()?.enqueue(object : Callback<List<UserInfo>> {
//            override fun onFailure(call: Call<List<UserInfo>>, t: Throwable) {
//                Log.d(
//                    "BuzzerServiceAPI::", "Failed API call with call: " + call +
//                            " + exception: " + t
//                )
//                activity.showToast("조회에 실패했습니다! 다시 시도해주세요!")
//            }
//
//            override fun onResponse(call: Call<List<UserInfo>>, response: Response<List<UserInfo>>) {
//                Log.d("Response:: ", response.body().toString())
////                if (response.code() == 200){
////                    activity.showToast("조회에 성공했습니다!")
////                    // activity.list = response.body()!!.data 같은 작업!
////                    activity.setResult(Activity.RESULT_OK)
////
////                }
////                else{ //400 -> message 내용에 따른 error handeling 필요함
////                    activity.showToast("조회에 실패했습니다!!")
////                }
//            }
//
//        })
    }

    /**
     *SignIn API를 통한 버 로그인
     *
     * @param bus_id - 가입할 bus id(번호판)
     *
     */

    fun signInBus(id: String, activity: SignInActivity) { //로컬디비에만 들어가는듯!
        //Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl(this.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //retrofit 객체를 통해 인터페이스 생성
        val service = retrofit.create(SignInService::class.java)

        //Body에 담을 데이터 생성
        val body = HashMap<String, String>()
        body.put("bus_id", id)
        service.signInBus(body)?.enqueue(object : Callback<Bus> {
            override fun onFailure(call: Call<Bus>, t: Throwable) {
                Log.d(
                    "SignInAPI::", "Failed API call with call: " + call +
                            " + exception: " + t
                )
                activity.showToast("버스 로그인에 실패했습니다! 다시 시도해주세요!")
            }

            override fun onResponse(call: Call<Bus>, response: retrofit2.Response<Bus>) {
                Log.d("Response:: ", response.toString())
                Log.d("Response:: ", response.body().toString())
                if (response.code() == 200){
                    activity.setBus(id,response.body()!!)
                    activity.showToast("로그인에 성공하였습니다.")
                    activity.setResult(Activity.RESULT_OK)
                    activity.changeActivity()
                }
                else if(response.code() == 401){ //400 -> message 내용에 따른 error handeling 필요함
                    //message는 error body에!
                    activity.showToast("회원 정보가 없습니다.")
                }
                else {
                    activity.showToast("로그인에 실패했습니다.")
                }
            }
        })
    }
    interface SignInService {
        @Headers(
            "accept: application/json",
            "content-type: application/json"
        )
        @GET("users/") //302: {bus id}
        fun getBookInfo(): Call<List<UserInfo>>

        @POST("accounts/user/signin")
        fun signInUser(
            @Body params: HashMap<String,String>
        ): Call<UserInfo>
        @POST("accounts/bus/signin")
        fun signInBus(
            @Body params: HashMap<String,String>
        ): Call<Bus>


    }




    /**
     * SignUp API를 통한 회원등록
     *
     * @param uid - 가입할 user id
     * @param upw - 가입할 user pw
     * @param uname - 가입할 user name
     *
     */

    fun signUpUser(id: String, pw: String, name:String, activity: SignUpActivity) {
        //Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl(this.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //retrofit 객체를 통해 인터페이스 생성
        val service = retrofit.create(SignUpService::class.java)

        //Body에 담을 데이터 생성

    //    val body = UserInfo(user_id = id, user_password = pw, user_name = name)
        val body = HashMap<String, String>()
        body.put("user_id", id)
        body.put("user_pw",pw)
        body.put("user_name",name)
        service.signUpUser(body)?.enqueue(object : Callback<Accepted> {
            override fun onFailure(call: Call<Accepted>, t: Throwable) {
                Log.d(
                    "SignUpAPI::", "Failed API call with call: " + call +
                            " + exception: " + t
                )
                activity.showToast("회원가입에 실패했습니다! 다시 시도해주세요!")
            }

            override fun onResponse(call: Call<Accepted>, response: retrofit2.Response<Accepted>) {
               // response.headers().toString()
                Log.d("Response:: ", response.toString())
                Log.d("Response:: ", response.headers().toString())
                Log.d("Response:: ", response.body().toString())
                if (response.code() == 200){
                    activity.showToast("회원가입이 완료되었습니다!")
                    activity.setResult(Activity.RESULT_OK)
                    activity.finish()
                }
                else{ //400 -> message 내용에 따른 error handeling 필요함
                    val msg = JSONObject(response.errorBody()?.string()).toString()
                    if (msg == "existed ID") {
                        activity.showToast("이미 존재하는 아이디입니다.")
                        //activity.setResult(Activity.re)
                        //activity.finish()
                    }
                    else{
                        activity.showToast("회원가입에 실패하였습니다.")
                    }
                  //  activity.showToast(response.body()!!.accepted["message"]!!)
                }
            }
        })
    }
    /**
     * SignUp API를 통한 회원등록
     *
     * @param bus_id - 가입할 bus id(번호판)
     * @param bus_route - 가입할 bus 노선번호
     *
     */

    fun signUpBus(id: String, route: String, activity: SignUpActivity) {
        //Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl(this.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //retrofit 객체를 통해 인터페이스 생성
        val service = retrofit.create(SignUpService::class.java)
        //Body에 담을 데이터 생성

        val body = HashMap<String, String>()
        body.put("bus_id", id)
        body.put("route_nm",route)

     //   val body = busInfo(id,route)
        service.signUpBus(body)?.enqueue(object : Callback<Accepted> {
            override fun onFailure(call: Call<Accepted>, t: Throwable) {
                Log.d(
                    "SignUpAPI::", "Failed API call with call: " + call +
                            " + exception: " + t
                )
                activity.showToast("회원가입에 실패했습니다! 다시 시도해주세요!")
            }

            override fun onResponse(call: Call<Accepted>, response: retrofit2.Response<Accepted>) {
                Log.d("Response:: ", response.toString())
                Log.d("Response:: ", response.headers().toString())
                Log.d("Response:: ", response.body().toString())
                Log.d("Response:: ",   response.message())
                if (response.code() == 200){
                    activity.showToast("회원가입에 성공하였습니다.")
                    activity.setResult(Activity.RESULT_OK)
                    activity.finish()
                }
                else{ //400 -> message 내용에 따른 error handeling 필요함
                    val msg = JSONObject(response.errorBody()?.string()).toString()
                    Log.d("Response:: ", msg)
                    if (msg == "existed ID"){
                        activity.showToast("이미 존재하는 id입니다.")
                    }
                    else{
                        activity.showToast("회원가입에 실패하였습니다.")
                    }

                }
            }
        })
    }
    interface SignUpService {
        @Headers(
            "accept: application/json",
            "content-type: application/json"
        )
        @POST("/accounts/user/signup")
        fun signUpUser(
            //@Header("apikey") apiKey: String,
            //@Header("appid") appID: String,
            @Body params: HashMap<String,String>
            //,
//            @Path("version") version: String,
//            @Path("uid") uid: String
        ): Call<Accepted>

        @POST("/accounts/bus/signup")
        fun signUpBus(
            //@Header("apikey") apiKey: String,
            //@Header("appid") appID: String,
            @Body params: HashMap<String,String>
            //
            //,
//            @Path("version") version: String,
//            @Path("uid") uid: String
        ): Call<Accepted>
    }
}

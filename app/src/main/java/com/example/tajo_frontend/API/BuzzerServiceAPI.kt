package com.example.tajo_frontend.API

import android.app.Activity
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import com.example.tajo_frontend.Data.Bell
import com.example.tajo_frontend.Activity.BellActivity
import com.example.tajo_frontend.Data.Station
import com.example.tajo_frontend.address
import com.example.tajo_frontend.port
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class BuzzerServiceAPI {
    //private val URL = "http://10.0.2.2:1337/"
    private val URL = address +":"+ port +"/"


    /**
     * BuzzerServiceAPI  통한 버스별 예약 조회
     */
    val Instance_Bell: BuzzzerBookInfoService by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(this.URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(BuzzzerBookInfoService::class.java)
    }
    fun getRouteStationInfo(route_nm:String,  callback: (List<Station>) -> Unit){


        val retrofit = Retrofit.Builder()
            .baseUrl(this.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(BuzzzerBookInfoService::class.java)
        service.getRouteStationInfo(route_nm)?.enqueue(object : Callback<List<Station>> {
            override fun onFailure(call: Call<List<Station>>, t: Throwable) {
                print("asdfadsf")
                Log.d(
                    "BuzzerServiceAPI::", "Failed API call with call: " + call +
                            " + exception: " + t
                )

            }
            override fun onResponse(call: Call<List<Station>>, response: Response<List<Station>>) {
                callback(response.body()!!)
                Log.d("Response:: ", response.body().toString() + "\n"+ response.code())
                if (response.code() == 200){

                }
                else{ //400 -> message 내용에 따른 error handeling 필요함

                }
            }
        })


    }
    fun getBellList(bus_id:String, activity: BellActivity? = null) {
        //Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl(this.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //retrofit 객체를 통해 인터페이스 생성
        val service = retrofit.create(BuzzzerBookInfoService::class.java)

        //Body에 담을 데이터 생성
        service.getBellInfo(bus_id)?.enqueue(object : Callback<List<Bell>> {
            override fun onFailure(call: Call<List<Bell>>, t: Throwable) {
                Log.d(
                    "BuzzerServiceAPI::", "Failed API call with call: " + call +
                            " + exception: " + t
                )
                activity?.showToast("조회에 실패했습니다! 다시 시도해주세요!")
            }
            override fun onResponse(call: Call<List<Bell>>, response: Response<List<Bell>>) {
                Log.d("Response:: ", response.body().toString() + "\n"+ response.code())
                if (response.code() == 200){
                    activity?.showToast("조회에 성공했습니다!")
                    val test = listOf<Bell>(Bell(user_id = "0",bus_id = "가가",stn_id = "1번",route_nm = "302")
                    ,Bell(user_id = "1",bus_id = "가가",stn_id = "2번",route_nm = "302")
                    ,Bell(user_id = "2",bus_id = "가가",stn_id = "3번",route_nm = "302")
                    ,Bell(user_id = "3",bus_id = "가가",stn_id = "3번",route_nm = "302")
                    ,Bell(user_id = "4",bus_id = "가가",stn_id = "2번",route_nm = "302")
                    ,Bell(user_id = "5",bus_id = "가가",stn_id = "1번",route_nm = "302")
                    ,Bell(user_id = "6",bus_id = "가가",stn_id = "4번",route_nm = "302")
                    )
                    val list = test.groupBy { it.stn_id } as MutableMap<String,List<Bell>> //나중에 test ->  response.body()!! 로 바꾸면 됨
                  //  list.remove("번")
                    activity?.setArray(list)
                    activity?.setResult(Activity.RESULT_OK)
                }
                else{ //400 -> message 내용에 따른 error handeling 필요함
                    activity?.showToast("조회에 실패했습니다!!")
                }
            }

        })
    }

    /**
     * BuzzerServiceAPI 통한 정류장 예약 정보 삭제
     */
    fun deleteBookInfo(bus_id: String, stn_id: String, activity: BellActivity, callback: (String) -> Unit) {
        //Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl(this.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //retrofit 객체를 통해 인터페이스 생성
        val service = retrofit.create(BuzzzerBookDeleteService::class.java)

        //Body에 담을 데이터 생성
        service.deleteBookInfo(
            bus_id,stn_id
        )?.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(
                    "BuzzerServiceAPI::", "Failed API call with call: " + call +
                            " + exception: " + t
                )
                activity.showToast("삭제 실패했습니다! 다시 시도해주세요!")
            }
            override fun onResponse(call: Call<String>, response: Response<String>) {
               // Log.d("Response:: ", response.body().toString())
                if (response.code() == 204){
                    callback("success")
                    activity.showToast("삭제에 성공했습니다!")
                    activity.setResult(Activity.RESULT_OK)
                }
                else{ //400 -> message 내용에 따른 error handeling 필요함
                    callback("fail")
                    activity.showToast("삭제에 실패했습니다!!")
                }
            }

        })
    }


    interface BuzzzerBookInfoService {
        @Headers(
            "accept: application/json",
            "content-type: application/json"
        )
        @GET("buzzer/{bus_id}") //302: {bus id}
        fun getBellInfo(@Path("bus_id")path: String): Call<List<Bell>>
        @GET("api/route-station/{route_nm}") //302: {bus id}
        fun getRouteStationInfo(@Path("route_nm")path: String): Call<List<Station>>
    }
    interface BuzzzerBookDeleteService {
        @Headers(
            "accept: application/json",
            "content-type: application/json"
        )
        @DELETE("buzzer/{bus_id}/{stn_id}") //302: {bus id}
        fun deleteBookInfo(@Path("bus_id")bus_id: String, @Path("stn_id")stn_id: String): Call<String>
    }


}
package com.example.tajo_frontend;

import com.example.tajo_frontend.API.BuzzerServiceAPI;
import org.junit.Test
//어떻게 비동기 task test 코드를 만들까!??!?!
class ApiTest {

    @Test
    fun getRouteStation(){
        val api = BuzzerServiceAPI()
        val route_nm = "302"
        api.getRouteStationInfo(route_nm)
    }
    @Test
    fun getBellInfo(){
        val api = BuzzerServiceAPI()
        val bus_id = "12가3456"
        api.getBellList(bus_id)
    }


}

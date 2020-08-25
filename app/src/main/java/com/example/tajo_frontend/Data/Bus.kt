package com.example.tajo_frontend.Data

import java.io.Serializable

/*
* route_nm: 버스번호(ex. 302)
* token: 토큰
* */
data class Bus(
    var bus_id:String, val route_nm:String,
    val token: String ): Serializable
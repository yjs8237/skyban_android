package com.nycompany.skyban

import com.nycompany.skyban.DTO.InoderDTO
import com.nycompany.skyban.DTO.LoginDTO
import retrofit2.Call
import retrofit2.http.*

interface ReqLogin {
    @POST("/user/usermngt/login")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<LoginDTO>

    /*
    @GET("/posts/{i}")
    fun getParamRequst(@Path("i")i:String):Call<LoginDTO>
    */
}

interface ReqOderList {
    @POST("/user/ordermngt/orderlist")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<InoderDTO>
}



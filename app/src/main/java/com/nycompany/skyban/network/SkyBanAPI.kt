package com.nycompany.skyban.network

import com.nycompany.skyban.dto.*
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
    fun postRequest(@Body body: String): Call<OderDTO>
}

interface ReqOrderRegister {
    @POST("/user/ordermngt/register")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<OrderRegisterDTO>
}

interface ReqOrderdetail{
    @POST("/user/ordermngt/orderdetail")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<InOrderdetailDTO>
}

interface ReqMyOrderList{
    @POST("/user/ordermngt/myorderlist")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<OderDTO>
}

interface ReqMyOrderDetail{
    @POST("/user/ordermngt/myorderdetail")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<InOrderdetailDTO>
}

interface ReqObtaInorder{
    @POST("/user/ordermngt/obtainorder")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<CommonDTO>
}

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

interface ReqPremium{
    @POST("/user/usermngt/premium")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<CommonDTO>
}

interface ReqPersonalInfoUpdateServer{
    @POST("/user/usermngt/update")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<CommonDTO>
}

interface ReqPointHistory{
    @POST("/user/usermngt/pointhistory")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<PointDTO>
}

interface ReqCashHistory{
    @POST("/user/cash/cashhistory")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<CashDTO>
}

interface ReqCashCharge{
    @POST("/user/cash/requestcharge")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<CommonDTO>
}

interface ReqCashRefund{
    @POST("/user/cash/refund")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<CommonDTO>
}

interface ReqNotificationList{
    @POST("/user/notification/requestlist")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<NotificationDTO>
}

interface ReqNotiDetail{
    @POST("/user/notification/detail")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<NotiDetailDTO>
}

interface ReqQna{
    @POST("/user/qna/list")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<QnalDTO>
}

interface ReqQnaDetail{
    @POST("/user/qna/detail")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<QnaDetailDTO>
}

interface ReqQnaRegister{
    @POST("/user/qna/register")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<CommonDTO>
}

interface ReqUpdatetoken{
    @POST("/user/usermngt/updatetoken")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<CommonDTO>
}

interface ReqUpdateDistance{
    @POST("/user/usermngt/distance")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<CommonDTO>
}

interface ReqLogout{
    @POST("/user/usermngt/logout")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<CommonDTO>
}

interface ReqLeave{
    @POST("/user/usermngt/leave")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<CommonDTO>
}

interface ReqRegister{
    @POST("/user/usermngt/register")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<CommonDTO>
}

interface ReqLocation{
    @POST("/user/usermngt/location")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<CommonDTO>
}

interface ReqUpdateorder{
    @POST("/user/ordermngt/updateorder")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<CommonDTO>
}

interface ReqRegisteragain{
    @POST("/user/ordermngt/registeragain")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<RegisteragainDTO>
}

interface ReqOrdercancle{
    @POST("/user/ordermngt/ordercancle")
    @Headers("Content-Type: application/json", "Authorization: Basic YXBpdXNlcjpTa3lCYW5BcGk=")
    fun postRequest(@Body body:String):Call<CommonDTO>
}

package com.nycompany.skyban.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.util.Log
import com.nycompany.skyban.MainActivity
import com.nycompany.skyban.R
import com.nycompany.skyban.dto.CommonDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.getUserinfo
import com.nycompany.skyban.isHaveUserinfo
import com.nycompany.skyban.network.ReqUpdatetoken
import com.nycompany.skyban.network.RetrofitCreater
import com.nycompany.skyban.util.ContextUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CustomFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage:RemoteMessage) {
        try {
            val pushDataMap = remoteMessage.data
            sendNotification(pushDataMap["title"], pushDataMap["msg"])
            //sendNotification(remoteMessage.notification?.title!!, remoteMessage.notification?.body!!)
        }catch (e:NullPointerException){
            Log.e(this::class.java.name, e.toString())
        }
    }

    private fun sendNotification(tile:String?, msg:String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val nBuilder = Notification.Builder(this)
                .setContentTitle(tile)
                .setContentText(msg)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(longArrayOf(1000, 1000))
                .setLights(Color.WHITE, 1500, 1500)
                .setContentIntent(contentIntent)

        val nManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nManager?.notify(0 /* ID of notification */, nBuilder.build())
    }

    override fun onNewToken(newToken: String?) {
        super.onNewToken(newToken)

        if(isHaveUserinfo()){
            val server = RetrofitCreater.getMyInstance()?.create(ReqUpdatetoken::class.java)
            var util = ContextUtil(this)

            val paramObject = JSONObject()
            paramObject.put("cell_no", getUserinfo()?.cell_no)
            paramObject.put("token", newToken)
            val reqString = paramObject.toString()

            server?.postRequest(reqString)?.enqueue(object: Callback<CommonDTO> {
                override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                    val msg = if(!util.isConnected()) getString(R.string.network_eror) else t.toString()
                    util.buildDialog("eror", msg).show()
                }

                override fun onResponse(call: Call<CommonDTO>, response: Response<CommonDTO>) {
                    response.body()?.let {
                        if(it.result == ResCode.Success.Code) {
                            Log.i(this::class.java.name, "update NewToaken")
                        }else{
                            Log.i(this::class.java.name, it.description)
                        }
                    }?:run{
                        Log.e(this::class.java.name, getString(R.string.response_body_eror))
                    }
                }
            })
        }
    }
}

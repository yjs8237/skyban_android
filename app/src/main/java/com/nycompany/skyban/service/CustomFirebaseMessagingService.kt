package com.nycompany.skyban.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import com.nycompany.skyban.MainActivity
import com.nycompany.skyban.R
import com.nycompany.skyban.dto.CommonDTO
import com.nycompany.skyban.enums.ResCode
import com.nycompany.skyban.getUserinfo
import com.nycompany.skyban.isHaveUserinfo
import com.nycompany.skyban.network.ReqUpdatetoken
import com.nycompany.skyban.network.RetrofitCreater
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CustomFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage:RemoteMessage) {
        try {
            val pushDataMap = remoteMessage.data
            makeBuilder(pushDataMap["title"], pushDataMap["message"]).apply {
                val nManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nManager?.notify(0 /* ID of notification */, build())
            }
            //sendNotification(remoteMessage.notification?.title!!, remoteMessage.notification?.body!!)
        }catch (e:NullPointerException){
            Log.e(this::class.java.name, e.toString())
        }
    }

    private fun makeBuilder(tile:String?, msg:String?):Notification.Builder {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        lateinit var nBuilder: Notification.Builder
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nBuilder = Notification.Builder(this, "order_01")
        } else {
            nBuilder = Notification.Builder(this)
        }

        val contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        nBuilder.setContentTitle(tile)
                .setContentText(msg)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)

        if (getUserinfo()?.isAlarmSound!! && (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)) {
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            nBuilder.setSound(defaultSoundUri)
                    .setVibrate(longArrayOf(1000, 1000))
                    .setLights(Color.WHITE, 1500, 1500)
                    .setContentTitle(tile)
                    .setContentText(msg)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent)
        }
        return nBuilder
    }

    override fun onNewToken(newToken: String?) {
        super.onNewToken(newToken)

        if(isHaveUserinfo()){
            val server = RetrofitCreater.getMyInstance()?.create(ReqUpdatetoken::class.java)

            val paramObject = JSONObject()
            paramObject.put("cell_no", getUserinfo()?.cell_no)
            paramObject.put("token", newToken)
            val reqString = paramObject.toString()

            server?.postRequest(reqString)?.enqueue(object: Callback<CommonDTO> {
                override fun onFailure(call: Call<CommonDTO>, t: Throwable) {
                    Log.e(this::class.java.name, t.toString())
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

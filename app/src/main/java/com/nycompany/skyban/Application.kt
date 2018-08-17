package com.nycompany.skyban

import android.app.*
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import com.beardedhen.androidbootstrap.TypefaceProvider
import io.realm.Realm
import android.os.Build



class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        TypefaceProvider.registerDefaultIconSets()
        Realm.init(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel("order_01", "발주상황", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "발주주문에 대한 알림"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.WHITE
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 100, 200)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val att = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            notificationChannel.setSound(Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.alret), att)

            notificationManager!!.createNotificationChannel(notificationChannel)
        }
    }
}


package com.nycompany.skyban

import android.app.Application
import android.content.Context
import com.beardedhen.androidbootstrap.TypefaceProvider
import io.realm.Realm
import android.net.ConnectivityManager.TYPE_WIFI
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.support.v7.app.AlertDialog


class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        TypefaceProvider.registerDefaultIconSets()
        Realm.init(this)
    }

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if ( cm.activeNetworkInfo == null) { // connected to the internet
            return false
        }
        return true
    }

    fun buildDialog(c: Context, title:String? ,msg: String?): AlertDialog.Builder {
        val builder = AlertDialog.Builder(c)
        builder.setTitle(title)
        builder.setMessage(msg)

        builder.setPositiveButton("Ok") { dialog, which -> dialog.dismiss() }

        return builder
    }
}


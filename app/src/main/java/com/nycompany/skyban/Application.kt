package com.nycompany.skyban

import android.app.Application
import android.content.Context
import com.beardedhen.androidbootstrap.TypefaceProvider
import io.realm.Realm
import android.net.ConnectivityManager
import android.support.v7.app.AlertDialog
import android.util.SparseArray

class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        TypefaceProvider.registerDefaultIconSets()
        Realm.init(this)
    }
}


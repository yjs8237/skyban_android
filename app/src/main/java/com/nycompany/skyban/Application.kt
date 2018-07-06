package com.nycompany.skyban

import android.app.Application
import com.beardedhen.androidbootstrap.TypefaceProvider
import io.realm.Realm

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        TypefaceProvider.registerDefaultIconSets()
        Realm.init(this)
    }
}
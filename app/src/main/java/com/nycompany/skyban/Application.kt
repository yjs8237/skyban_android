package com.nycompany.skyban

import android.app.Application
import com.beardedhen.androidbootstrap.TypefaceProvider

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        TypefaceProvider.registerDefaultIconSets()
    }
}

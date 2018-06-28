package com.nycompany.skyban

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launcher)

        var mHandler = Handler()
        mHandler.postDelayed({
            startActivity(Intent().setClass(this@LauncherActivity, LoginActivity::class.java))
            finish()
        }, 1000)
    }
}

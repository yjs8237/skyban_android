package com.nycompany.skyban

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launcher)

        if(isHaveUserinfo()) {
           updateUserInfo(getUserinfo()?.cell_no, getUserinfo()?.password)
        }

        var mHandler = Handler()
        mHandler.postDelayed({
            //Realm.deleteRealm(Realm.getDefaultConfiguration())
            if(isHaveUserinfo()) startActivity(Intent().setClass(this@LauncherActivity, MainActivity::class.java))
            else startActivity(Intent().setClass(this@LauncherActivity, LoginActivity::class.java))
            finish()
        }, 600)
    }
}

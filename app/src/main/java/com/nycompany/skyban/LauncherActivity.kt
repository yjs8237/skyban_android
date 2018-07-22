package com.nycompany.skyban

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.nycompany.skyban.dto.RealmUserInfo
import com.nycompany.skyban.util.ContextUtil
import io.realm.Realm

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launcher)
        val realm = Realm.getDefaultInstance()
        val data = realm.where(RealmUserInfo::class.java).findAll()
        if(data.size != 0) ContextUtil(this).updateUserInfo()

        var mHandler = Handler()
        mHandler.postDelayed({
            //Realm.deleteRealm(Realm.getDefaultConfiguration())
            if(data.size == 0) startActivity(Intent().setClass(this@LauncherActivity, LoginActivity::class.java))
            else  startActivity(Intent().setClass(this@LauncherActivity, MainActivity::class.java))
            realm.close()
            finish()
        }, 600)
    }
}

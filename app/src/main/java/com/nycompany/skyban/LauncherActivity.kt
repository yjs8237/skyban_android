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

        if(UserInfoLocal.isHaveUserinfo()) {
            UserInfoLocal.updateUserInfo()
        }

        var mHandler = Handler()
        mHandler.postDelayed({
            //Realm.deleteRealm(Realm.getDefaultConfiguration())
            if(UserInfoLocal.isHaveUserinfo()) startActivity(Intent().setClass(this@LauncherActivity, LoginActivity::class.java))
            else  startActivity(Intent().setClass(this@LauncherActivity, MainActivity::class.java))
            finish()
        }, 600)
    }
}

package com.nycompany.skyban

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.nycompany.skybanminitp.ResCode
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var app:Application = Application()
        var context:Context = this
        if(!app.isConnected(this)) app.buildDialog(this, "eror",getString(R.string.network_eror)).show()

        setContentView(R.layout.activity_login)

        val server = RetrofitCreater.getInstance(this)?.create(ReqLogin::class.java)
        loginBtn.setOnClickListener{
            val paramObject = JSONObject()

            editTextPhone.setText("01032228237")
            paramObject.put("cell_no", editTextPhone.text)
            paramObject.put("user_pwd", editTextPassword.text)
            var reqString = paramObject.toString()

            server?.postRequest(reqString)?.enqueue(object:Callback<LoginDTO>{
                override fun onFailure(call: Call<LoginDTO>?, t: Throwable?) {
                    var msg = if(!app.isConnected(context)) getString(R.string.network_eror) else t.toString()
                    app.buildDialog(context, "eror", msg).show()
                }

                override fun onResponse(call: Call<LoginDTO>?, response: Response<LoginDTO>?) {
                    if(response?.body()?.result == ResCode.Success.Code) {
                        Realm.getDefaultInstance().use {
                            val data = it.where(RealmUserInfo::class.java).findAll()
                            it.beginTransaction()
                            if (data.size > 0) it.deleteAll()
                            var userInfo = setUserinfo(response)
                            it.copyToRealm(userInfo)
                            it.commitTransaction()
                        }
                        startActivity(Intent().setClass(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }else{
                        app.buildDialog(context, "eror", response?.body()?.description).show()
                    }
                }
            })
        }

        regBtn.setOnClickListener{
            Realm.getDefaultInstance().use {
                it.beginTransaction()
                it.deleteAll()
                it.commitTransaction()
            }
            startActivity(Intent().setClass(this, termsActivity::class.java))
        }
    }

    fun setUserinfo(response:Response<LoginDTO>?): RealmUserInfo{
        val userInfo:RealmUserInfo = RealmUserInfo().apply {
            cell_no = response?.body()?.user?.cell_no
            user_type = response?.body()?.user?.user_type
            user_name = response?.body()?.user?.user_name
            user_level = response?.body()?.user?.user_level
            order_point = response?.body()?.user?.order_point
            obtain_point = response?.body()?.user?.obtain_point
            cash = response?.body()?.user?.cash
            location = response?.body()?.user?.location
            cop_number = response?.body()?.user?.cop_number
            email = response?.body()?.user?.email
            latitude = response?.body()?.user?.latitude
            longitude = response?.body()?.user?.longitude
            car_type = response?.body()?.user?.car_type
            car_length = response?.body()?.user?.car_length
            car_height = response?.body()?.user?.car_height
            op_invertor = response?.body()?.user?.op_invertor
            longitude = response?.body()?.user?.longitude
            op_guljul = response?.body()?.user?.op_guljul
            op_winchi = response?.body()?.user?.op_winchi
            op_danchuk = response?.body()?.user?.op_danchuk
            recomm_cell_no = response?.body()?.user?.recomm_cell_no
            order_cnt = response?.body()?.user?.order_cnt
            obtain_cnt = response?.body()?.user?.obtain_cnt
            reg_date = response?.body()?.user?.reg_date
        }
        return userInfo
    }
}


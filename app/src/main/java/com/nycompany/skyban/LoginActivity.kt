package com.nycompany.skyban

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val server = RetrofitCreater.getInstance(this)?.create(ReqLogin::class.java)
        loginBtn.setOnClickListener{
            val paramObject = JSONObject()

            editTextPhone.setText("01032228237")
            editTextPassword.setText("1234")
            paramObject.put("cell_no", editTextPhone.text)
            paramObject.put("user_pwd", editTextPassword.text)
            var reqString = paramObject.toString()

            server?.postRequest(reqString)?.enqueue(object:Callback<LoginDTO>{
                override fun onFailure(call: Call<LoginDTO>?, t: Throwable?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<LoginDTO>?, response: Response<LoginDTO>?) {
                    val realm = Realm.getDefaultInstance()
                    val data = realm.where(RealmUserInfo::class.java).findAll()

                    if(data.size == 0){
                        var userInfo = setUserinfo(response)
                        realm.beginTransaction()
                        realm.copyToRealm(userInfo)
                        realm.commitTransaction()
                    }
                    realm.close()
                }
            })
        }

        regBtn.setOnClickListener{
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            realm.deleteAll()
            realm.commitTransaction()
            realm.close()
            startActivity(Intent().setClass(this, termsActivity::class.java))
        }
    }

    fun setUserinfo(response:Response<LoginDTO>?): RealmUserInfo{
        val userInfo:RealmUserInfo = RealmUserInfo()
        userInfo.cell_no = response?.body()?.user?.cell_no
        userInfo.user_type = response?.body()?.user?.user_type
        userInfo.user_name = response?.body()?.user?.user_name
        userInfo.user_level = response?.body()?.user?.user_level
        userInfo.order_point = response?.body()?.user?.order_point
        userInfo.obtain_point = response?.body()?.user?.obtain_point
        userInfo.cash = response?.body()?.user?.cash
        userInfo.location = response?.body()?.user?.location
        userInfo.cop_number = response?.body()?.user?.cop_number
        userInfo.email = response?.body()?.user?.email
        userInfo.latitude = response?.body()?.user?.latitude
        userInfo.longitude = response?.body()?.user?.longitude
        userInfo.car_type = response?.body()?.user?.car_type
        userInfo.car_length = response?.body()?.user?.car_length
        userInfo.car_height = response?.body()?.user?.car_height
        userInfo.op_invertor = response?.body()?.user?.op_invertor
        userInfo.longitude = response?.body()?.user?.longitude
        userInfo.op_guljul = response?.body()?.user?.op_guljul
        userInfo.op_winchi = response?.body()?.user?.op_winchi
        userInfo.op_danchuk = response?.body()?.user?.op_danchuk
        userInfo.recomm_cell_no = response?.body()?.user?.recomm_cell_no
        userInfo.order_cnt = response?.body()?.user?.order_cnt
        userInfo.obtain_cnt = response?.body()?.user?.obtain_cnt
        userInfo.reg_date = response?.body()?.user?.reg_date

        return userInfo
    }
}

